package com.polarj.common.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polarj.common.CommonConstant;
import com.polarj.common.security.JWTUtils;
import com.polarj.common.utility.FieldValueUtil;
import com.polarj.common.utility.Image2Base64Util;
import com.polarj.common.utility.report.FieldNameAndHeaderMapping;
import com.polarj.common.utility.xls.ExcelReportFileReaderWriter;
import com.polarj.common.web.model.ClientRequest;
import com.polarj.common.web.model.ServerResponse;
import com.polarj.common.web.model.WebErrorStatus;
import com.polarj.common.web.service.WorkflowViewService;
import com.polarj.model.FieldSpecification;
import com.polarj.model.Functionality;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.ModelSpecification;
import com.polarj.model.UserAccount;
import com.polarj.model.UserAccountConfig;
import com.polarj.model.UserAccountRole;
import com.polarj.model.component.FileInfo;
import com.polarj.model.enumeration.FunctionType;
import com.polarj.model.enumeration.ModelOperation;
import com.polarj.model.enumeration.UserAccountStatus;
import com.polarj.model.service.EntityService;
import com.polarj.model.service.FieldSpecificationService;
import com.polarj.model.service.ModelSpecificationService;
import com.polarj.model.service.UserAccountRoleService;
import com.polarj.model.service.UserAccountService;

public abstract class ModelController<M extends GenericDbInfo, ID extends Serializable, S extends EntityService<M, ID>>
        extends BaseController
{
    @Autowired
    protected S service;

    @Autowired
    private ModelSpecificationService modelSpecService;

    @Autowired
    private FieldSpecificationService fieldSpecService;

    @Autowired
    private UserAccountRoleService roleService;

    @Autowired
    private UserAccountService uaService;

    @Autowired(required = false)
    protected WorkflowViewService wfViewService;

    protected Class<M> modelClazz;

    public ModelController(Class<M> clazz)
    {
        super();
        this.modelClazz = clazz;
    }

    protected UserAccount getLoginUserAccount(String apiToken)
    {
        UserAccount ua = getLoginUserAccount();
        if (ua != null)
        {
            return ua;
        }
        if (StringUtils.isEmpty(apiToken))
        {
            return null;
        }
        String hashedLoginName = JWTUtils.getHashedLoginName(apiToken, logger);
        if (StringUtils.isEmpty(hashedLoginName))
        {
            return null;
        }
        ua = uaService.fetchByHashedName(hashedLoginName);
        if (ua != null && !UserAccountStatus.ACTIVE.name().equalsIgnoreCase(ua.getStatus()))
        {
            return null;
        }
        if (!JWTUtils.verify(apiToken, hashedLoginName, ua.getApiPassword(), logger))
        {
            return null;
        }
        if (ua.getMenus() != null)
        {
            return ua;
        }
        List<UserAccountRole> roles = ua.getAllRoles();
        if (roles.size() == 0)
        {
            logger.info("User: {} does not belong to any role.", ua.getLoginName());
            return ua;
        }
        uaService.generateUserAccFunctionalities(ua, roles);
        uaService.addSuperAdminFlag(ua);
        return ua;
    }

    // 获取模型和模型属性的说明信息
    @GetMapping("metadata")
    public @ResponseBody ResponseEntity<ServerResponse<ModelSpecification>> getModelMetadata()
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted(ModelOperation.READ.name()))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }
        try
        {
            List<ModelSpecification> reses = modelSpecService.fetchAllSubModelSpecificationByClassName(
                    modelClazz.getName(), userAcc.getUserSetting().getViewLang());

            for (ModelSpecification res : reses)
            {
                mergeWithUserAuthorization(userAcc, res);
                Class<?> mClass = null;
                try
                {
                    mClass = Class.forName(res.getClassName());
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                    mClass = null;
                }
                if (mClass == null)
                {
                    continue;
                }
                // 这是使用service替换modelSpecService,可以进行对业务model进行重写getFieldMetaData方法
                // 例如一些选项FieldSpecification需要单独个性化处理
                List<FieldSpecification> fsfs = service.getFieldMetaData(mClass, userAcc,
                        userAcc.getUserSetting().getViewLang());
                res.setFieldSpecs(fsfs);
                generateUserAccFunctionalities(res, userAcc.getAllRoles(), userAcc.getUserSetting().getViewLang());
            }
            return generateRESTFulResponse(reses);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    private void generateUserAccFunctionalities(ModelSpecification mSpec, List<UserAccountRole> roles,
            String languageId)
    {
        List<Functionality> classOps = new ArrayList<>();
        List<Functionality> instanceOps = new ArrayList<>();
        for (UserAccountRole ur : roles)
        {
            UserAccountRole urInfo = roleService.getById(ur.getId(), 3, languageId);
            List<Functionality> roleFuncs = urInfo.getFunctions();
            for (Functionality roleFunc : roleFuncs)
            {
                String functionalityCode = roleFunc.getCode();
                int modelClazzNameIndex = functionalityCode.lastIndexOf(".");
                String modelClazzName = functionalityCode.substring(0, modelClazzNameIndex);
                switch (FunctionType.valueOf(roleFunc.getType()))
                {
                case MODEL_CLASS_OPERATION:
                    if (modelClazzName.equals(modelClazz.getName()))
                    {
                        classOps.add(roleFunc);
                    }
                    break;
                case MODEL_INSTANCE_OPERATION:
                    if (modelClazzName.equals(modelClazz.getName()))
                    {
                        instanceOps.add(roleFunc);
                    }
                    break;
                default:
                    break;
                }
            }
        }
        mSpec.setModelFunctionalities(classOps);
        mSpec.setObjectFunctionalities(instanceOps);
    }

    private boolean isAdminRoleUser(UserAccount userAcc)
    {
        List<UserAccountRole> roles = userAcc.getAllRoles();
        if (roles != null)
        {
            for (UserAccountRole role : roles)
            {
                if ("admin".equals(role.getCode()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private void mergeWithUserAuthorization(UserAccount userAcc, ModelSpecification mSpec)
    {
        if (isAdminRoleUser(userAcc))
        {
            return;
        }
        List<Functionality> modelOps = userAcc.getModelOperations();
        String modelClassName = mSpec.getClassName();
        if (modelOps == null || modelOps.size() == 0)
        {
            // 设计上，只有admin用户才没有设置模型的操作权限，只需要按照模型本身的要求操作即可。
            return;
        }
        boolean creatable = false;
        boolean deletable = false;
        boolean updatable = false;
        boolean readable = false;
        for (Functionality modelOp : modelOps)
        {
            String modelOpCode = modelOp.getCode();
            if (modelOpCode.startsWith(modelClassName + "."))
            {
                String op = modelOpCode.substring(modelClassName.length() + 1);
                ModelOperation mOper = ModelOperation.valueOf(op);
                switch (mOper)
                {
                case CREATE:
                    creatable = true;
                    break;
                case DELETE:
                    deletable = true;
                    break;
                case READ:
                    readable = true;
                    break;
                case UPDATE:
                    updatable = true;
                    break;
                }
            }
        }
        // QUES: 注意，在这里mSpec的属性值被修改了，如果是使用缓存的话，注意需要先clone一个新的才行。
        mergeWithUserAuthorization(mSpec, creatable, deletable, updatable, readable);
    }

    private void mergeWithUserAuthorization(ModelSpecification mSpec, boolean creatable, boolean deletable,
            boolean updatable, boolean readable)
    {
        boolean b = mSpec.getAddible() == null ? false : mSpec.getAddible();
        mSpec.setAddible(b && creatable);
        b = mSpec.getBatchDeletable() == null ? false : mSpec.getBatchDeletable();
        mSpec.setBatchDeletable(b && deletable);
        b = mSpec.getCloneable() == null ? false : mSpec.getCloneable();
        mSpec.setCloneable(b && creatable);
        b = mSpec.getDeletable() == null ? false : mSpec.getDeletable();
        mSpec.setDeletable(b && deletable);
        b = mSpec.getDownloadable() == null ? false : mSpec.getDownloadable();
        mSpec.setDownloadable(b && readable);
        b = mSpec.getUpdatable() == null ? false : mSpec.getUpdatable();
        mSpec.setUpdatable(b && updatable);
        b = mSpec.getUploadable() == null ? false : mSpec.getUploadable();
        mSpec.setUploadable(b && creatable);
    }

    // 获取指定id的模型
    @GetMapping("{id}")
    public @ResponseBody ResponseEntity<ServerResponse<M>> getEntity(@PathVariable ID id)
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted(ModelOperation.READ.name()))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }
        try
        {
            M m = null;
            if (!userAcc.isSuperAdmin())
            {
                m = getUserEntityById(id, userAcc.getId(), userAcc.getUserSetting().getViewLang());
            }
            else
            {
                m = service.getById(id, userAcc.getUserSetting().getViewLang());
            }
            return generateRESTFulResponse(m);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    // 更新指定id的模型
    @PostMapping("{id}")
    public @ResponseBody ResponseEntity<ServerResponse<M>> updateEntity(@PathVariable ID id,
            @RequestBody ClientRequest<M> clientRequest)
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted(ModelOperation.UPDATE.name()))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }
        try
        {
            M entityWithUpdatedInfo = clientRequest.getData();
            M m = null;
            if (!userAcc.isSuperAdmin())
            {
                m = updateUserEntity(id, entityWithUpdatedInfo, userAcc.getId(),
                        userAcc.getUserSetting().getViewLang());
            }
            else
            {
                m = updateEntity(id, entityWithUpdatedInfo, userAcc.getId(), userAcc.getUserSetting().getViewLang());
            }
            // 这里本来放在service.update方法里面校验的，发现会有问题。
            // 工作流会每一步都会进行更新，造成多次update校验，不合理。造成性能损耗。
            // 所以放在modelController实现，做成通用的更新校验，如果业务service没有重写，则直接返回null跳过校验
            // 数据校验后明明已经更新数据
            String error = service.validateEntity(m, userAcc.getId(), userAcc.getUserSetting().getViewLang());
            if (error != null)
            {
                m = updateEntity(id, m, userAcc.getId(), userAcc.getUserSetting().getViewLang());
            }
            // 工作流里面的这个校验GenerateUpdatingResult是否可以去掉了，避免重复校验？
            if (wfViewService != null)
            {
                // 有个bug怎么也找不到，数据校验后明明已经更新数据了，
                // 但是经过wfViewService.UPDATABLE后数据回滚了,更改的数据还原了，但是新增的数据增加了?
                wfViewService.workflowForUserOperation(m,
                        GenerateUpdatingResult(m, userAcc.getId(), userAcc.getUserSetting().getViewLang()),
                        userAcc.getId());
            }
            if (error != null)
            {
                return generateRESTFulResponse(m, error);
            }
            else
            {
                // 初级解决方案,添加返回成功提示信息
                // todo 待出错信息国际化框架做好后,,进行重构...
                return generateRESTFulSuccResponse(m, "Update Entity Success.");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    // // 子类使用这个方法来给前端返回验证业务模型的结果
    // protected String validateEntity(M m)
    // {
    // return null;
    // }

    protected String GenerateUpdatingResult(M m, Integer operId, String languageId)
    {
        return "UPDATED";
    }

    // 删除模型的多个记录（只传递id）
    @PostMapping("remove-request") // url: remove-request?ids=1,2,3
    public @ResponseBody ResponseEntity<ServerResponse<Boolean>> removeEntities(@RequestParam List<ID> ids)
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted(ModelOperation.DELETE.name()))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }
        try
        {
            if (ids == null || ids.size() == 0)
            {
                return generateRESTFulResponse(false);
            }
            boolean b = true;
            for (ID id : ids)
            {
                if (!userAcc.isSuperAdmin())
                {
                    b = b && removeUserEntity(id, userAcc.getId(), userAcc.getUserSetting().getViewLang());
                }
                else
                {
                    service.delete(id, userAcc.getId());
                    b = b && true;
                }
            }
            return generateRESTFulResponse(b);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    // 删除指定id的模型
    @DeleteMapping("{id}")
    public @ResponseBody ResponseEntity<ServerResponse<Boolean>> removeEntity(@PathVariable ID id)
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted(ModelOperation.DELETE.name()))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }
        try
        {
            // QUES: 在这里判断不能删除自己的帐号是不是不对呢？
            if (modelClazz.equals(UserAccount.class) && userAcc.getId().equals(id))
            {
                logger.info("You can not remove yourself.");
                return generateRESTFulResponse(Boolean.TRUE);
            }
            // QUES: 是不是到delelte方法中做判断比较好？
            boolean b = false;
            if (!userAcc.isSuperAdmin())
            {
                b = removeUserEntity(id, userAcc.getId(), userAcc.getUserSetting().getViewLang());
            }
            else
            {
                service.delete(id, userAcc.getId());
                b = true;
            }
            return generateRESTFulResponse(b);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    // 针对有文件类型属性的业务模型，在新增数据之前，先上传文件
    // QUES: 目前支持一个业务模型只附加一个文件属性（可以有多个文件），如果有多个文件属性，如何区分彼此
    @PostMapping(value = "beforeCreation", consumes = "multipart/form-data") // url:
                                                                             // beforeCreation?id=
    public @ResponseBody ResponseEntity<ServerResponse<String>> beforeCreateEntity(
            @RequestParam(name = "id", required = false) Integer instanceId,
            MultipartHttpServletRequest multipleFileRequest)
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted(ModelOperation.UPDATE.name()) && !isPermitted(ModelOperation.CREATE.name()))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }
        try
        {
            Integer id = instanceId == null ? 0 : instanceId;
            String savedFileName = saveUploadedFileLocally(multipleFileRequest, userAcc, id);
            return generateRESTFulResponse(savedFileName.substring(savedFileName.lastIndexOf('/') + 1));
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    // 创建模型的新数据
    // 创建模型的时候，同时启动模型对应的工作流
    // QUES：需要导入动作流的是不是模型说明中加管理模型的工作流名为属性？
    // QUES：是不是能够支持一个模型被多个工作流管理？但是同时只能有一个在工作或者能够同时工作？
    @PostMapping("")
    public @ResponseBody ResponseEntity<ServerResponse<M>> createEntity(@RequestBody ClientRequest<M> clientRequest)
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted(ModelOperation.CREATE.name()))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }
        try
        {
            M model = clientRequest.getData();
            addOwnerFor(model, userAcc);
            M result = service.create(model, userAcc.getId(), userAcc.getUserSetting().getViewLang());
            if (wfViewService != null)
            {
                wfViewService.workflowForCreatingModel(result, service, userAcc.getId());
            }
            if (result != null && result.getId() != null)
            {
                moveUploadedFileToInstanceFolder(result);
            }
            // 将validateEntity 放到service，校验逻辑放到service中
            String error = service.validateEntity(result, userAcc.getId(), userAcc.getUserSetting().getViewLang());
            if (error != null)
            {
                return generateRESTFulResponse(result, error);
            }
            else
            {
                // 初级解决方案,添加返回成功提示信息
                // todo 待出错信息国际化框架做好后,,进行重构...
                return generateRESTFulSuccResponse(result, "Create Entity Success.");
            }

        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    private void moveUploadedFileToInstanceFolder(M model)
    {
        String fileName = null;
        String srcPath = getPermanentFilePath(modelClazz, 0);
        String destPath = getPermanentFilePath(modelClazz, model.getId());
        Field[] fields = FieldValueUtil.getAllFields(modelClazz, logger);
        for (Field field : fields)
        {
            Class<?> fieldType = FieldValueUtil.getTypeOfField(field, logger);
            if (FileInfo.class.isAssignableFrom(fieldType))
            {
                field.setAccessible(true);
                FileInfo fi = null;
                try
                {
                    fi = (FileInfo) field.get(model);
                    if (fi != null)
                    {
                        fileName = fi.getFileName();
                        FileUtils.moveFile(new File(srcPath + fileName), new File(destPath + fileName));
                    }
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    // 按照条件查找数据
    @PostMapping("search") // url: search?field=&desc=true|false
    public @ResponseBody ResponseEntity<ServerResponse<M>> searchByCriteria(
            @RequestParam(name = "field", required = false) String sortField,
            @RequestParam(name = "desc", required = false) boolean sortDesc,
            @RequestBody ClientRequest<M> clientRequest)
    {
        UserAccount userAcc = getLoginUserAccount(clientRequest.getApiToken());
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted("READ"))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }
        try
        {
            M searchCriteria = clientRequest.getData();
            if (searchCriteria == null)
            {
                searchCriteria = modelClazz.newInstance();
            }
            if (searchCriteria.getPageIndex() == null)
            {
                searchCriteria.setPageIndex(1);
            }
            if (searchCriteria.getCurPageSize() == null)
            {
                searchCriteria.setCurPageSize(10);
            }
            Page<M> res = null;
            if (!userAcc.isSuperAdmin())
            {
                res = searchUserEntities(searchCriteria, sortField, sortDesc, userAcc.getId(),
                        userAcc.getUserSetting().getViewLang(), 2);
            }
            else
            {
                res = service.listByCriteria(searchCriteria, sortField, sortDesc,
                        userAcc.getUserSetting().getViewLang(), 2);
            }
            return generateRESTFulResponse(res);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    @GetMapping("downloadtemplate")
    public void downloadUploadTemplate(HttpServletResponse resp)
    {
        String fileName = modelClazz.getSimpleName().toLowerCase() + "-template.xls";
        String path = getPermanentFilePath(null, null);
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            sendFileToPage(path, CommonConstant.BlankFileName + ".xls", resp);
        }
        else
        {
            sendFileToPage(path, fileName, resp);
        }
    }

    @GetMapping("downloadFile") // url: downloadFile?fileName=&id=
    public void downloadFile(@RequestParam(required = true) String fileName, @RequestParam(required = false) Integer id,
            HttpServletResponse resp)
    {
        String path;
        UserAccount userAcc = getLoginUserAccount();
        try
        {
            // QUES： 文件上传时，据说文件名会转成国际标准的编码格式，所以需要做一个编码格式转换
            fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            fileName = "";
        }
        if (id == null)
        {
            path = getTemporaryFilePath();
        }
        else
        {
            path = getPermanentFilePath(modelClazz, id);
        }
        if (userAcc == null)
        {
            sendFileToPage(path, CommonConstant.BlankFileName + fileName.substring(fileName.lastIndexOf('.') + 1),
                    resp);
        }
        else
        {
            sendFileToPage(path, fileName, resp);
        }
    }

    @GetMapping("getImgUrl")
    public @ResponseBody ResponseEntity<ServerResponse<String>> getImgUrl(
            @RequestParam(required = true) String fileName, @RequestParam(required = false) Integer id,
            HttpServletResponse resp)
    {
        String path;
        try
        {
            // QUES： 文件上传时，据说文件名会转成国际标准的编码格式，所以需要做一个编码格式转换
            fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            fileName = "";
        }
        if (id == null)
        {
            path = getTemporaryFilePath();
        }
        else
        {
            path = getPermanentFilePath(modelClazz, id);
        }
        String wholeName = path + fileName;
        String imgBase64 = Image2Base64Util.ImageToBase64ByLocal(wholeName);
        return generateRESTFulResponse(imgBase64);
    }

    // 下载满足条件的所有数据， 需要有数据总量的限制，在模型描述中有这个参数，如果超过限制，只下载相应的数量数据
    @PostMapping("downloadAllData") // url:
                                    // downloadAllData?field=&desc=true|false
    public @ResponseBody ResponseEntity<ServerResponse<String>> downloadAllData(
            @RequestParam(name = "field", required = false) String sortField,
            @RequestParam(name = "desc", required = false) boolean sortDesc,
            @RequestBody ClientRequest<M> clientRequest)
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted(ModelOperation.READ.name()))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }
        try
        {
            M searchCriteria = clientRequest.getData();
            List<M> entities = new ArrayList<M>();
            ModelSpecification mSpec = modelSpecService.fetchModelSpecificationByClassName(modelClazz.getName(),
                    userAcc.getUserSetting().getViewLang());
            if (mSpec.getMaximumAmountForDownload() > 100)
            {
                searchCriteria.setCurPageSize(100);
            }
            else
            {
                searchCriteria.setCurPageSize(mSpec.getMaximumAmountForDownload());
            }
            int pageIndex = 1;
            int totalPages = Integer.MAX_VALUE;
            int totalRecordsDownloaded = 0;
            searchCriteria.setPageIndex(pageIndex);
            while (pageIndex < totalPages && totalRecordsDownloaded < mSpec.getMaximumAmountForDownload())
            {
                Page<M> res = null;
                if (!userAcc.isSuperAdmin())
                {
                    res = searchUserEntities(searchCriteria, sortField, sortDesc, userAcc.getId(),
                            userAcc.getUserSetting().getViewLang(), null);
                }
                else
                {
                    res = service.listByCriteria(searchCriteria, sortField, sortDesc,
                            userAcc.getUserSetting().getViewLang(), null);
                }
                if (!res.hasContent())
                {
                    break;
                }
                entities.addAll(res.getContent());
                totalPages = res.getTotalPages();
                pageIndex++;
                totalRecordsDownloaded = totalRecordsDownloaded + 100;
            }
            String generateFileName = generateExcelFile(entities, getTemporaryFilePath(), userAcc,
                    userAcc.getUserSetting());
            return generateRESTFulResponse(generateFileName);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    // 从搜索的结果中选择需要的下载，虽然限定了一次搜索的数据量，
    // 还是需要检查是否超出某个模型的实时下载限制
    // QUES：如果没有数据，是不是应该给前端发送一个空文件？
    @GetMapping("downloadSelectedData") // url:
                                        // downloadSelectedData?entityIds=1,2,3
    public @ResponseBody ResponseEntity<ServerResponse<String>> downloadSelectedData(@RequestParam List<ID> entityIds)
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted(ModelOperation.READ.name()))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }
        try
        {
            List<M> entities = null;
            if (!userAcc.isSuperAdmin())
            {
                entities = listUserEntities(entityIds, userAcc.getId(), userAcc.getUserSetting().getWorkLang());
            }
            else
            {
                entities = service.list(entityIds, userAcc.getUserSetting().getWorkLang());
            }
            String generateFileName = null;
            if (entities != null && entities.size() > 0)
            {
                generateFileName = generateExcelFile(entities, getTemporaryFilePath(), userAcc,
                        userAcc.getUserSetting());
            }
            return generateRESTFulResponse(generateFileName);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    @PostMapping(value = "upload", consumes = "multipart/form-data")
    public @ResponseBody ResponseEntity<ServerResponse<Integer>> uploadModelData(
            MultipartHttpServletRequest multipleFileRequest)
    {
        UserAccount userAcc = getLoginUserAccount();
        if (userAcc == null)
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noUserError);
        }
        if (!isPermitted(ModelOperation.CREATE.name()))
        {
            return generateErrorRESTFulResponse(WebErrorStatus.noRightError);
        }

        try
        {
            Integer num = saveTheDataInUploadFile(multipleFileRequest, userAcc, userAcc.getUserSetting().getWorkLang());
            return generateRESTFulSuccResponse(num, "Upload " + num + "Items Success");
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return generateErrorRESTFulResponse(e.getMessage());
        }
    }

    // 返回值是系统中保存的文件，文件名格式为：上传文件夹，模型名称以及以毫秒为后缀的时间，当前操作者，原始文件名
    protected String saveUploadedFileLocally(MultipartHttpServletRequest multipleFileRequest, UserAccount userAcc,
            Integer instanceId) throws IOException
    {
        Map<String, MultipartFile> fileMap = multipleFileRequest.getFileMap();

        if (fileMap == null || fileMap.size() == 0)
        {
            return null;
        }
        String res = null;
        for (MultipartFile file : fileMap.values())
        {
            if (file == null)
            {
                continue;
            }
            String fileName = file.getOriginalFilename();
            try
            {
                // QUES： 文件上传时，据说文件名会转成国际标准的编码格式，所以需要做一个编码格式转换
                fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
            }
            catch (Exception e)
            {
                logger.error(e.getMessage(), e);
                fileName = "";
            }
            // 1. 上传的数据保存到本地
            String destFileName = null;
            if (instanceId != null)
            {
                destFileName = getPermanentFilePath(modelClazz, instanceId) + fileName;
            }
            else
            {
                destFileName = getTemporaryFilePath() + modelClazz.getSimpleName() + "_"
                        + CommonConstant.DateTimeFormatWithMicroSecondForFileName.format(new Date()) + "_"
                        + userAcc.getId() + "_" + fileName;
            }
            File fDest = new File(destFileName);
            file.transferTo(fDest);
            res = destFileName;
        }
        return res;
    }

    private Integer saveTheDataInUploadFile(MultipartHttpServletRequest multipleFileRequest, UserAccount userAcc,
            String languageId) throws IOException
    {
        String destFileName = saveUploadedFileLocally(multipleFileRequest, userAcc, null);
        if (destFileName.endsWith(CommonConstant.xlsxSuffix) || destFileName.endsWith(CommonConstant.xlsSuffix))
        {
            // 2. 以本地读取的方式，把数据读出，然后持久化到数据库
            List<M> entities = readDataFromExcelFile(destFileName, languageId, modelClazz, userAcc);
            if (entities != null && entities.size() > 0)
            {
                addOwnerFor(entities, userAcc);
                // 处理各业务模型需要特殊处理的相关逻辑方法
                service.dealWithEntitys(entities);
                List<M> addEntities = service.create(entities, userAcc.getId(), languageId);
                if (wfViewService != null && addEntities != null && addEntities.size() > 0)
                {
                    for (M entity : addEntities)
                    {
                        wfViewService.workflowForCreatingModel(entity, service, userAcc.getId());
                    }
                }
                // 3. 删除保存的文件
                File fDest = new File(destFileName);
                fDest.delete();
                return addEntities == null ? 0 : addEntities.size();
            }
        }
        return 0;
    }

    // QUES: 在测试的时候，临时改成public的方法，测试没有问题，还是要设置成private
    protected <X> List<X> readDataFromExcelFile(String fileName, String languageId, Class<X> modelClazz,
            UserAccount userAcc)
    {
        if (fileName == null)
        {
            logger.info("No data for create model data.");
            return null;
        }
        List<FieldSpecification> fsfs = fieldSpecService.getFieldMetaData(modelClazz, userAcc, languageId);
        if (fsfs == null || fsfs.size() == 0)
        {
            logger.info("Can not get metadata for: {}.", modelClazz.getName());
            return null;
        }
        ExcelReportFileReaderWriter<X> reader = new ExcelReportFileReaderWriter<X>(
                generateFieldNameAndHeaderMappingFrom(fsfs, null, true));
        List<X> entities = null;
        try
        {
            entities = reader.readReportFile(modelClazz, fileName);
            // TODO: entities中的独立管理的业务类属性目前只有唯一性属性中有值，需要到数据库中读出所有的数据
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            entities = null;
        }
        return entities;
    }

    // QUES: 在测试的时候，临时改成public的方法，测试没有问题，还是要设置成private
    private String generateExcelFile(List<M> entities, String downloadPath, UserAccount userAcc,
            UserAccountConfig userSetting)
    {
        if (entities == null || entities.size() == 0)
        {
            logger.info("No model data for generating report.");
            return null;
        }
        Class<?> clazz = entities.get(0).getClass();
        List<FieldSpecification> fsfs = fieldSpecService.getFieldMetaData(clazz, userAcc,
                userSetting == null ? null : userSetting.getWorkLang());
        if (fsfs == null || fsfs.size() == 0)
        {
            return null;
        }
        ExcelReportFileReaderWriter<M> writer = new ExcelReportFileReaderWriter<M>(
                generateFieldNameAndHeaderMappingFrom(fsfs, userSetting == null ? null : userSetting.getHiddenFields(),
                        false));
        String modelName = fsfs.get(0).getClassFullName();
        String fileName = modelName.substring(modelName.lastIndexOf(".") + 1) + "-"
                + CommonConstant.DateTimeFormatWithMicroSecondForFileName.format(new Date()) + CommonConstant.xlsSuffix;
        try
        {
            writer.writeReportFile(entities, downloadPath + fileName);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            fileName = null;
        }
        return fileName;
    }

    private FieldNameAndHeaderMapping generateFieldNameAndHeaderMappingFrom(List<FieldSpecification> fsfs,
            String userHideFields, boolean isUpload)
    {
        FieldNameAndHeaderMapping fieldNameAndHeaderMapping = new FieldNameAndHeaderMapping();
        fieldNameAndHeaderMapping = generateFieldNameAndHeaderMappingFrom("", "", fsfs, userHideFields,
                fieldNameAndHeaderMapping, isUpload);
        return fieldNameAndHeaderMapping;
    }

    // 在下载时，需要根据需要隐藏信息，并不出现在报告中（isUpload=false）。
    // 在上传时，所有表头和属性的对应关系都要准备好（isUpload=true），QUES：最好做好不可为空的标志。
    // 对于嵌入式的属性字段（如：MoneyInfo等）的处理：
    // 1. 前端使用formatter的描述来显示
    // 2. 后端使用formatter的描述解码
    private FieldNameAndHeaderMapping generateFieldNameAndHeaderMappingFrom(String compFieldName, String compFieldLabel,
            List<FieldSpecification> fsfs, String userHideFieldsString,
            FieldNameAndHeaderMapping fieldNameAndHeaderMapping, boolean isUpload)
    {
        if (fsfs == null || fsfs.size() == 0)
        {
            return fieldNameAndHeaderMapping;
        }
        List<String> userHideFields = listUserHideFields(fsfs.get(0).getClassFullName(), userHideFieldsString);
        for (FieldSpecification fsf : fsfs)
        {
            List<FieldSpecification> compfsfs = fsf.getComponentMetaDatas();

            if (fsf.isEmbedded() || compfsfs == null || compfsfs.size() == 0)
            {
                if (isUpload || (!fsf.getHide() && !userHideFields.contains(fsf.getName())))
                {
                    fieldNameAndHeaderMapping.addFieldNameAndHeaderPair(compFieldName + fsf.getName(),
                            compFieldLabel + fsf.getLabel());
                }
            }
            else
            {
                if (!fsf.isHasDetail())
                {
                    String labelField = "";
                    if (fsf.getLabelField() != null && fsf.getLabelField().length() > 0)
                    {
                        labelField = "(" + fsf.getLabelField() + ")";
                    }
                    if (fsf.getUniqueField() != null && fsf.getUniqueField().length() > 0)
                    {
                        labelField = "(" + fsf.getUniqueField() + ")";
                    }
                    fieldNameAndHeaderMapping.addFieldNameAndHeaderPair(compFieldName + fsf.getName() + labelField,
                            compFieldLabel + fsf.getLabel());
                }
                else
                {
                    fieldNameAndHeaderMapping = generateFieldNameAndHeaderMappingFrom(fsf.getName() + ".",
                            fsf.getLabel() + ".", compfsfs, userHideFieldsString, fieldNameAndHeaderMapping, isUpload);
                }
            }
        }
        return fieldNameAndHeaderMapping;
    }

    private List<String> listUserHideFields(String modelFullName, String userHideFields)
    {
        List<String> res = new ArrayList<String>();

        String modelName = modelFullName.substring(modelFullName.lastIndexOf(".") + 1, modelFullName.length());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = null;
        if (userHideFields == null || userHideFields.length() == 0)
        {
            return res;
        }
        try
        {
            rootNode = objectMapper.readValue(userHideFields, JsonNode.class);
            JsonNode modelNode = rootNode.get(modelName);
            if (modelNode != null)
            {
                String[] hiddenFields = modelNode.asText().split(",");
                if (hiddenFields == null || hiddenFields.length == 0)
                {
                    return res;
                }
                for (String s : hiddenFields)
                {
                    res.add(s.trim());
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            rootNode = null;
        }
        return res;
    }

    protected boolean isOperatable(ModelOperation modelOperator, UserAccount userAcc)
    {
        if (isAdminRoleUser(userAcc))
        {
            return true;
        }
        List<Functionality> modelOpers = userAcc.getModelOperations();
        if (modelOpers == null || modelOpers.size() == 0)
        {
            return true;
        }
        for (Functionality modelOper : modelOpers)
        {
            if (modelOper.getCode().equalsIgnoreCase(modelClazz.getName() + "." + modelOperator.name()))
            {
                return true;
            }
        }
        return false;
    }

    // 对于需要限制用户访问的业务模型，需要重写下列这些方法，已经在拥有owner属性的controller类中处理了。
    // ========================================================================================================
    protected M getUserEntityById(ID id, Integer operId, String languageId)
    {
        return service.getById(id, languageId);
    }

    protected M updateUserEntity(ID id, M entityWithUpdatedData, Integer operId, String languageId)
    {
        return service.update(id, entityWithUpdatedData, operId, languageId);
    }

    protected boolean removeUserEntity(ID id, Integer operId, String languageId)
    {
        service.delete(id, operId);
        return true;
    }

    protected Page<M> searchUserEntities(M searchCriteria, String sortField, boolean sortDesc, Integer operId,
            String languageId, Integer dataLevel)
    {
        return service.listByCriteria(searchCriteria, sortField, sortDesc, languageId, dataLevel);
    }

    protected List<M> listUserEntities(List<ID> ids, Integer operId, String languageId)
    {
        return service.list(ids, languageId);
    }

    protected void addOwnerFor(M model, UserAccount ua)
    {

    }

    protected void addOwnerFor(List<M> models, UserAccount ua)
    {
        if (models == null || models.size() == 0)
        {
            return;
        }
        for (M model : models)
        {
            addOwnerFor(model, ua);
        }
    }

    // 对于model模型有jsonview等字段保存的数据更新时进行特殊处理
    // ========================================================================================================
    protected M updateEntity(ID id, M entityWithUpdatedData, Integer operId, String languageId)
    {
        return service.update(id, entityWithUpdatedData, operId, languageId);
    }

    protected boolean isPermitted(String operateName)
    {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null && currentUser.isAuthenticated())
        {
            if (currentUser.hasRole("admin"))
            {
                return true;
            }
            return currentUser.isPermitted(modelClazz.getName() + "." + operateName);
        }
        return false;
    }
}
