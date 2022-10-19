package campus.bankAccount;

public class AccoundFields {

    private String id;
    private String name;
    private String iban;
    private String integrationCode;
    private String schoolId;
    private String currency;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getIntegrationCode() {
        return integrationCode;
    }

    public void setIntegrationCode(String integrationCode) {
        this.integrationCode = integrationCode;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "AccoundFields{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", iban='" + iban + '\'' +
                ", integrationCode='" + integrationCode + '\'' +
                ", schoolId='" + schoolId + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
