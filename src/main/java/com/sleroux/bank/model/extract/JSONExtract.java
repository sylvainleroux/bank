package  com.sleroux.bank.model.extract;

import java.util.List;

import com.sleroux.bank.model.operation.Operation;

public class JSONExtract{

    private String compte; 
    private Integer format_version;
    private String date;
    private List<Operation> operations;


    /**
     * @return the compte
     */
    public String getCompte() {
        return compte;
    }

    /**
     * @param compte the compte to set
     */
    public void setCompte(String compte) {
        this.compte = compte;
    }

    /**
     * @param format_version the format_version to set
     */
    public void setFormat_version(Integer format_version) {
        this.format_version = format_version;
    }

    /**
     * @param datetime the datetime to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @param operations the operations to set
     */
    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }
   
    /**
     * @return the operations
     */
    public List<Operation> getOperations() {
        return operations;
    }
}