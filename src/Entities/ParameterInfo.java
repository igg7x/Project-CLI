package Entities;

public class ParameterInfo {

    private String parameterName;
    private String dataType;

    public ParameterInfo(String parameterName, String dataType) {
        this.parameterName = parameterName;
        this.dataType = dataType;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getDataType() {
        return dataType;
    }

    @Override
    public String toString() {
        return "ParameterInfo [dataType=" + dataType + ", parameterName=" + parameterName + "]";
    }
}
