package com.leadmaster;

/**
 * Created by User on 18/11/2016.
 */
public class AddRecord_Data
{
    private String FieldID;
    private String fieldName;
    private String partitionName;
    private String FieldType;
    private String FieldDataType;
    private String FieldRestriction;

    public String getFieldID() {
        return FieldID;
    }

    public void setFieldID(String fieldID) {
        FieldID = fieldID;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return FieldType;
    }

    public void setFieldType(String fieldType) {
        FieldType = fieldType;
    }

    public String getPartitionName() {
        return partitionName;
    }

    public void setPartitionName(String partitionName) {
        this.partitionName = partitionName;
    }

    public String getFieldDataType() {
        return FieldDataType;
    }

    public void setFieldDataType(String fieldDataType) {
        FieldDataType = fieldDataType;
    }

    public String getFieldRestriction() {
        return FieldRestriction;
    }

    public void setFieldRestriction(String fieldRestriction) {
        FieldRestriction = fieldRestriction;
    }

    @Override
    public String toString() {
        return fieldName;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof AddRecord_Data)
        {
            AddRecord_Data c = (AddRecord_Data)obj;
            if(c.getFieldName().equals(fieldName) && c.getFieldType().equals(fieldName) && c.getPartitionName().equals(partitionName) && c.getFieldID().equals(FieldID) && c.getFieldDataType().equals(FieldDataType) && c.getFieldRestriction().equals(FieldRestriction))
                return true;
        }

        return false;
    }
}
