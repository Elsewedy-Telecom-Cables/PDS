package com.etc.pdsapp.dao;

import com.etc.pdsapp.excel.file.AssemblyFile;
import com.etc.pdsapp.excel.file.BraidFile;
import com.etc.pdsapp.excel.processor.AssemblyProcessor;
import com.etc.pdsapp.excel.service.AssemblyService;
import com.etc.pdsapp.excel.validator.AssemblyValidator;

public class AppContext {

    private static final AppContext INSTANCE = new AppContext();

    private AppContext() {

    }
    private static final MachineDao machineDao = new MachineDao();
    private static final SectionDao sectionDao = new SectionDao();
    private static final UserDao userDao = new UserDao();
    private static final StageDao stageDao = new StageDao();
    private static final AssemblyDao assemblyDao = new AssemblyDao();
    private static final BraidDao braidDao = new BraidDao();
    private static final AssemblyFile assemblyFile = new AssemblyFile();
    private static final BraidFile braidFile = new BraidFile();
    private static final AssemblyProcessor assemblyProcessor = new AssemblyProcessor();
    private static final AssemblyService assemblyService = new AssemblyService();
    private static final AssemblyValidator assemblyValidator = new AssemblyValidator();




    public static AppContext getInstance() {
        return INSTANCE;
    }

    public AssemblyDao getAssemblyDao() {
        return assemblyDao;
    }
    public BraidDao getBraidDao() {
        return braidDao;
    }
    public MachineDao getMachineDao() {
        return machineDao;
    }
    public SectionDao getSectionDao() {
        return sectionDao;
    }
    public UserDao getUserDao() {
        return userDao;
    }
    public StageDao getStageDao() {
        return stageDao;
    }
    public AssemblyFile getAssemblyFile() {
        return assemblyFile;
    }
    public BraidFile getBraidFile() {
        return braidFile;
    }
    public AssemblyProcessor getAssemblyProcessor() {
        return assemblyProcessor;
    }
    public AssemblyService getAssemblyService() {
        return assemblyService;
    }
    public AssemblyValidator getAssemblyValidator() {
        return assemblyValidator;
    }




}
