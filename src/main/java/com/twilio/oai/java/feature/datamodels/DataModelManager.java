package com.twilio.oai.java.feature.datamodels;

import org.openapitools.codegen.CodegenModel;

import java.util.List;

public class DataModelManager {

    public static DataModelManager instance;
    List<DataModel> dataModels;
    
    private DataModelManager() {
        dataModels = List.of(
            new OneOf()
        );
    }
    
    public static synchronized DataModelManager getInstance() {
        if (instance == null) {
            synchronized (DataModelManager.class) {
                if (instance == null) {
                    instance = new DataModelManager();
                }
            }
        }
        return instance;
    }

    public void apply(final CodegenModel codegenModel) {
        for (DataModel dataModel: dataModels) {
            if (dataModel.shouldApply(codegenModel)) {
                dataModel.apply(codegenModel);
                return; // Can a CodegenModel have multiple datamodels at same level, if yes don't return ?
            }
        }
    }
    
    public boolean shouldApply(final CodegenModel codegenModel) {
        for (DataModel dataModel : dataModels) {
            if (dataModel.shouldApply(codegenModel)) {
                return true;
            }
        }
        return false;
    }
}
