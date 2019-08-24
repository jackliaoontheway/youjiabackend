package com.polarj;

import javax.sql.DataSource;

import org.copperengine.core.audit.BatchingAuditTrail;
import org.copperengine.core.batcher.RetryingTxnBatchRunner;
import org.copperengine.core.batcher.impl.BatcherImpl;
import org.copperengine.core.common.DefaultProcessorPoolManager;
import org.copperengine.core.common.JdkRandomUUIDFactory;
import org.copperengine.core.persistent.MySqlDialect;
import org.copperengine.core.persistent.PersistentPriorityProcessorPool;
import org.copperengine.core.persistent.PersistentScottyEngine;
import org.copperengine.core.persistent.ScottyDBStorage;
import org.copperengine.core.persistent.txn.CopperTransactionController;
import org.copperengine.core.util.BackchannelDefaultImpl;
import org.copperengine.core.wfrepo.URLClassloaderClasspathProvider;
import org.copperengine.ext.wfrepo.classpath.ClasspathWorkflowRepository;
import org.copperengine.spring.SpringDependencyInjector;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.polarj.workflow.model.repository.CopperWaitRepos;
import com.polarj.workflow.model.service.impl.WorkflowSpecServiceImpl;
import com.polarj.workflow.service.impl.WorkflowManagementServiceCopperImpl;
import com.polarj.workflow.service.impl.WorkflowServiceCopperImpl;

@Component
@ConditionalOnProperty("workflow.enable")
public class CopperConfig
{
    @Bean(initMethod = "start", destroyMethod = "shutdown")
    @Scope("singleton")
    public ClasspathWorkflowRepository wfRepository()
    {
        return new ClasspathWorkflowRepository("com.polarj.workflow.copper");
    }

    @Bean(initMethod = "startup")
    @Scope("singleton")
    public MySqlDialect dbDialect(ClasspathWorkflowRepository wfRepository)
    {
        MySqlDialect mySqlDialect = new MySqlDialect();
        mySqlDialect.setWfRepository(wfRepository);
        mySqlDialect.setMultiEngineMode(false);
        mySqlDialect.setEngineId("engine001");
        return mySqlDialect;
    }

    @Bean
    @Scope("singleton")
    public ScottyDBStorage persistentDBStorage(MySqlDialect dialect, CopperTransactionController txController,
            BatcherImpl batcher)
    {
        ScottyDBStorage dbStorage = new ScottyDBStorage();
        dbStorage.setTransactionController(txController);
        dbStorage.setDialect(dialect);
        dbStorage.setBatcher(batcher);
        dbStorage.setCheckDbConsistencyAtStartup(true);
        return dbStorage;
    }

    @Bean
    @Scope("singleton")
    public CopperTransactionController txController(@Qualifier("wfDataSource") DataSource wfDataSource)
    {
        CopperTransactionController txController = new CopperTransactionController();
        txController.setDataSource(wfDataSource);
        return txController;
    }

    @Bean(initMethod = "startup", destroyMethod = "shutdown")
    @Scope("singleton")
    public BatcherImpl batcher(@Qualifier("wfDataSource") DataSource wfDataSource)
    {
        BatcherImpl batcher = new BatcherImpl(10);
        RetryingTxnBatchRunner<?, ?> rtbr = new RetryingTxnBatchRunner<>();
        rtbr.setDataSource(wfDataSource);
        batcher.setBatchRunner(rtbr);
        return batcher;
    }

    @Bean
    @Scope("singleton")
    public PersistentPriorityProcessorPool persistentProcessorPoolDefault(CopperTransactionController txController)
    {
        PersistentPriorityProcessorPool persistentPriorityProcessorPool = new PersistentPriorityProcessorPool(
                "P#DEFAULT", txController, 48);
        persistentPriorityProcessorPool.setDequeueBulkSize(100);
        persistentPriorityProcessorPool.setLowerThreshold(96);
        persistentPriorityProcessorPool.setUpperThreshold(200);
        return persistentPriorityProcessorPool;
    }

    @Bean
    @Scope("singleton")
    public DefaultProcessorPoolManager<PersistentPriorityProcessorPool> persistentPPManager(
            PersistentPriorityProcessorPool ppp)
    {
        DefaultProcessorPoolManager<PersistentPriorityProcessorPool> ppManager = new DefaultProcessorPoolManager<>();
        ppManager.addProcessorPool(ppp);
        return ppManager;
    }

    @Bean(initMethod = "startup", destroyMethod = "shutdown")
    @Scope("singleton")
    public PersistentScottyEngine persistentEngine(
            DefaultProcessorPoolManager<PersistentPriorityProcessorPool> ppManager, ScottyDBStorage dbStorage,
            ClasspathWorkflowRepository wfRepository)
    {
        PersistentScottyEngine engine = new PersistentScottyEngine();
        engine.setIdFactory(new JdkRandomUUIDFactory());
        engine.setProcessorPoolManager(ppManager);
        engine.setDependencyInjector(new SpringDependencyInjector());
        engine.setDbStorage(dbStorage);
        engine.setWfRepository(wfRepository);
        return engine;
    }

    @Bean(initMethod = "startup")
    @Scope("singleton")
    public BatchingAuditTrail auditTrail(@Qualifier("wfDataSource") DataSource wfDataSource, BatcherImpl batcher)
    {
        BatchingAuditTrail auditTrail = new BatchingAuditTrail();
        auditTrail.setDataSource(wfDataSource);
        auditTrail.setBatcher(batcher);
        return auditTrail;
    }

    @Bean
    public BackchannelDefaultImpl workflowBackChannel()
    {
        return new BackchannelDefaultImpl();
    }

    @Bean
    public URLClassloaderClasspathProvider urlCompileOption()
    {
        return new URLClassloaderClasspathProvider();
    }

    @Bean
    public WorkflowManagementServiceCopperImpl workflowManagementService(PersistentScottyEngine engine,
            WorkflowSpecServiceImpl wfSpecService, BackchannelDefaultImpl workflowBackChannel,
            CopperWaitRepos copperWaitRepos)
    {
        WorkflowManagementServiceCopperImpl workflowManagementService = new WorkflowManagementServiceCopperImpl();
        workflowManagementService.setEngine(engine);
        workflowManagementService.setWfSpecService(wfSpecService);
        workflowManagementService.setWorkflowBackChannel(workflowBackChannel);
        workflowManagementService.setCopperWaitRepos(copperWaitRepos);
        return workflowManagementService;
    }

    public WorkflowServiceCopperImpl workflowService(PersistentScottyEngine engine,
            WorkflowSpecServiceImpl wfSpecService, BackchannelDefaultImpl workflowBackChannel)
    {
        WorkflowServiceCopperImpl workflowService = new WorkflowServiceCopperImpl();
        workflowService.setEngine(engine);
        workflowService.setWfSpecService(wfSpecService);
        workflowService.setWorkflowBackChannel(workflowBackChannel);
        return workflowService;
    }
}
