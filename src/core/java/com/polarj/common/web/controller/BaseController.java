package com.polarj.common.web.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.polarj.PolarjAppConfig;
import com.polarj.common.CommonConstant;
import com.polarj.common.utility.CryptoUtil;
import com.polarj.common.utility.SpringContextUtils;
import com.polarj.common.web.WebBaseConstant;
import com.polarj.common.web.model.ServerResponse;
import com.polarj.common.web.model.WebErrorStatus;
import com.polarj.common.web.model.WebSuccessStatus;
import com.polarj.model.GenericDbInfo;
import com.polarj.model.UserAccount;
import com.polarj.model.service.EntityService;

public abstract class BaseController
{
    protected final Logger logger;

    @Resource
    private PolarjAppConfig config;

    public BaseController()
    {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    protected UserAccount getLoginUserAccount()
    {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null && currentUser.isAuthenticated())
        {
            return (UserAccount) currentUser.getPrincipal();
        }
        return null;
    }

    protected <T extends GenericDbInfo, ID extends Serializable> EntityService<T, ID> getService(Class<T> clazz)
    {
        EntityService<T, ID> service = SpringContextUtils.getModelServiceBean(clazz);
        return service;
    }

    protected void refreshJavaSessionId(HttpServletRequest request)
    {
        request.getSession().invalidate();
        request.getSession(true);
    }

    protected void setApplicationContextFileSystemPath(HttpServletRequest request)
    {
        if (WebBaseConstant.ApplicationContextFileSystemPath == null)
        {
            String path = request.getServletContext().getRealPath("/");
            WebBaseConstant.ApplicationContextFileSystemPath = path;
        }
    }

    protected <T> ResponseEntity<ServerResponse<T>> generateErrorRESTFulResponse(String errMsg)
    {
        ServerResponse<T> serverResp = new ServerResponse<T>();
        serverResp.addError(new WebErrorStatus(errMsg));
        serverResp.setNonceToken(CryptoUtil.generateHash(Long.toString(new Date().getTime())));
        return new ResponseEntity<ServerResponse<T>>(serverResp, HttpStatus.OK);
    }

    protected <T> ResponseEntity<ServerResponse<T>> generateErrorRESTFulResponse(WebErrorStatus err)
    {
        ServerResponse<T> serverResp = new ServerResponse<T>();
        serverResp.addError(err);
        serverResp.setNonceToken(CryptoUtil.generateHash(Long.toString(new Date().getTime())));
        return new ResponseEntity<ServerResponse<T>>(serverResp, HttpStatus.OK);
    }

    protected <T> ResponseEntity<ServerResponse<T>> generateHttpErrorRESTFulResponse(HttpStatus httpStatus)
    {
        ServerResponse<T> serverResp = new ServerResponse<T>();
        serverResp.addError(new WebErrorStatus(httpStatus.name()));
        serverResp.setNonceToken(CryptoUtil.generateHash(Long.toString(new Date().getTime())));
        return new ResponseEntity<ServerResponse<T>>(serverResp, httpStatus);
    }

    protected <T> ResponseEntity<ServerResponse<T>> generateRESTFulResponse(T res)
    {
        return generateRESTFulResponse(res, null);
    }

    protected <T> ResponseEntity<ServerResponse<T>> generateRESTFulResponse(T res, String errMsg)
    {
        ServerResponse<T> serverResp = new ServerResponse<T>();
        serverResp.addData(res);
        if (StringUtils.isNotEmpty(errMsg))
        {
            serverResp.addError(new WebErrorStatus(errMsg));
        }
        serverResp.setNonceToken(CryptoUtil.generateHash(Long.toString(new Date().getTime())));
        return new ResponseEntity<ServerResponse<T>>(serverResp, HttpStatus.OK);
    }

    protected <T> ResponseEntity<ServerResponse<T>> generateRESTFulSuccResponse(T res, String succMsg)
    {
        ServerResponse<T> serverResp = new ServerResponse<T>();
        serverResp.addData(res);
        if (StringUtils.isNotEmpty(succMsg))
        {
            serverResp.addInfo(new WebSuccessStatus(succMsg));
        }
        serverResp.setNonceToken(CryptoUtil.generateHash(Long.toString(new Date().getTime())));
        return new ResponseEntity<ServerResponse<T>>(serverResp, HttpStatus.OK);
    }

    protected <T> ResponseEntity<ServerResponse<T>> generateRESTFulResponse(List<T> res)
    {
        return generateRESTFulResponse(res, null);
    }

    protected <T> ResponseEntity<ServerResponse<T>> generateRESTFulResponse(List<T> res, String errMsg)
    {
        ServerResponse<T> serverResp = new ServerResponse<T>();
        serverResp.addDatas(res);
        if (StringUtils.isNotEmpty(errMsg))
        {
            serverResp.addError(new WebErrorStatus(errMsg));
        }
        serverResp.setNonceToken(CryptoUtil.generateHash(Long.toString(new Date().getTime())));
        return new ResponseEntity<ServerResponse<T>>(serverResp, HttpStatus.OK);
    }

    protected <T> ResponseEntity<ServerResponse<T>> generateRESTFulResponse(Page<T> res)
    {
        ServerResponse<T> serverResp = new ServerResponse<T>();
        if (res != null)
        {
            serverResp.setTotalRecords(res.getTotalElements());
            serverResp.setTotalPages(res.getTotalPages());
            serverResp.setCurrentPageIndex(res.getNumber());
            serverResp.setPageSize(res.getNumberOfElements());
            serverResp.addDatas(res.getContent());
            serverResp.setNonceToken(CryptoUtil.generateHash(Long.toString(new Date().getTime())));
        }
        return new ResponseEntity<ServerResponse<T>>(serverResp, HttpStatus.OK);
    }

    /*
     * usage: res = serviceMethodProxy(service, "methodNameInTheService", new
     * Class<?>[] { argsType }, new Object[] { args });
     */

    protected <T, S> ResponseEntity<ServerResponse<T>> serviceMethodProxy(S service, String methodName,
            Class<?>[] parameterTypes, Object[] args)
    {
        HttpStatus httpStatus = HttpStatus.OK;

        WebErrorStatus err = null;

        ServerResponse<T> res = new ServerResponse<T>();

        class DynamicProxyHandler implements InvocationHandler
        {
            private Object proxied;

            public DynamicProxyHandler(Object proxied)
            {
                this.proxied = proxied;
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
            {
                return method.invoke(proxied, args);
            }
        }

        try
        {
            @SuppressWarnings("unchecked")
            S proxy = (S) Proxy.newProxyInstance(service.getClass().getClassLoader(),
                    service.getClass().getInterfaces(), new DynamicProxyHandler(service));

            Method method = service.getClass().getMethod(methodName, parameterTypes);

            @SuppressWarnings("unchecked")
            T data = (T) method.invoke(proxy, args);

            res.addData(data);
        }
        catch (AuthenticationException e)
        {
            err = new WebErrorStatus("web.unauth", "Incorrect user name or password!");
            logger.error(e.getMessage(), e);
            httpStatus = HttpStatus.UNAUTHORIZED;
        }
        catch (AuthorizationException e)
        {
            err = new WebErrorStatus("web.unauth", "Can not access those resources!");
            logger.error(e.getMessage(), e);
            httpStatus = HttpStatus.UNAUTHORIZED;
        }
        catch (Exception e)
        {
            err = new WebErrorStatus("system.error", e.getMessage());
            logger.error(e.getMessage(), e);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        res.addError(err);
        return new ResponseEntity<ServerResponse<T>>(res, httpStatus);
    }

    protected void sendFileToPage(String path, String fileName, HttpServletResponse resp)
    {
        if (fileName.toLowerCase().endsWith(".csv"))
        {
            sendFileToPage(path, fileName, "text/csv", resp);
        }
        else if (fileName.toLowerCase().endsWith(".xls"))
        {
            sendFileToPage(path, fileName, "application/vnd.ms-excel", resp);
        }
        else if (fileName.toLowerCase().endsWith(".xlsx"))
        {
            sendFileToPage(path, fileName, "application/x-excel", resp);
        }
        else if (fileName.toLowerCase().endsWith(".txt"))
        {
            sendFileToPage(path, fileName, "text/plain", resp);
        }
        else if (fileName.toLowerCase().endsWith(".xml"))
        {
            sendFileToPage(path, fileName, "application/xml", resp);
        }
        else if (fileName.toLowerCase().endsWith(".pdf"))
        {
            sendFileToPage(path, fileName, "application/pdf", resp);
        }
        else if (fileName.toLowerCase().endsWith(".png"))
        {
            sendFileToPage(path, fileName, "image/png", resp);
        }
        else if (fileName.toLowerCase().endsWith(".html"))
        {
            sendFileToPage(path, fileName, "text/plain", resp);
        }
        else
        {
            sendFileToPage(path, fileName, "application/octet-stream", resp);
        }
    }

    private void sendFileToPage(String path, String fileName, String contentType, HttpServletResponse resp)
    {
        String wholeName = path + fileName;
        logger.trace("Will download file: " + wholeName);
        File file = new File(wholeName);
        if (!file.exists())
        { // 如果指定的文件不存在，发送给前端一个空白文件
            logger.error("Can not find file {} for download.", fileName);
            // wholeName = config.getPermanentFilePath() +
            // CommonConstant.BlankFileName ;
            wholeName = getPermanentFilePath(null, null) + CommonConstant.BlankFileName
                    + fileName.substring(fileName.lastIndexOf('.') + 1);
            file = new File(wholeName);
            contentType = "application/pdf";
        }
        ServletOutputStream sOutputStream = null;
        try
        {
            sOutputStream = resp.getOutputStream();
        }
        catch (Exception e)
        {
            sOutputStream = null;
            logger.error(e.getMessage(), e);
        }
        if (sOutputStream == null)
        {
            return;
        }
        resp.setContentType(contentType);
        resp.setHeader("Content-Disposition", "inline; filename=" + fileName);
        resp.setHeader("Content-Length", String.valueOf(file.length()));

        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
        }
        catch (Exception e)
        {
            fis = null;
            logger.error(e.getMessage(), e);
        }
        if (fis == null)
        {
            return;
        }
        BufferedInputStream bufis = new BufferedInputStream(fis);
        byte[] cacheBuf = new byte[1024];
        long readLength = 0l;

        try
        {
            while (readLength < file.length())
            {
                int j = bufis.read(cacheBuf, 0, 1024);
                readLength += j;
                sOutputStream.write(cacheBuf, 0, j);
            }
            sOutputStream.flush();
            sOutputStream.close();
            resp.flushBuffer();
            bufis.close();
            fis.close();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        sOutputStream = null;
    }

    protected String getTemporaryFilePath()
    {
        return config.getTemporaryFilePath();
    }

    protected String getFrontendBaseURL()
    {
        return config.getFrontendBaseURL();
    }

    // 业务模型中的文件属性，永久保存文件的路径格式：/path to permanent/model class simple
    // name/instance id/filename
    // 其他永久保存文件的路径格式： /path to permanent/
    // 1. 业务模型的上传模板
    // 2.
    protected <T extends GenericDbInfo> String getPermanentFilePath(Class<T> clazz, Integer instanceId)
    {
        if (clazz != null && instanceId != null)
        {
            String wholePath = config.getPermanentFilePath() + clazz.getSimpleName() + "/" + instanceId + "/";
            File f = new File(wholePath);
            if (f.exists() && f.isDirectory())
            {
                return wholePath;
            }
            if (f.mkdirs())
            {
                return wholePath;
            }
        }
        return config.getPermanentFilePath();
    }

}
