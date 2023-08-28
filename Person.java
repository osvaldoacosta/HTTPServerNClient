import java.io.IOException;
import java.util.Map;

public class Person {
    private String nome;
    private Integer idade;
    private String email;

    public Person(String nome, Integer idade, String email) {
        this.nome = nome;
        this.idade = idade;
        this.email = email;
    }

    public Person() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void fromJSON(String jsonString) throws IOException {
        Map<String, Object> jsonMap = HandlerJSON.parseJson(jsonString);
        System.out.println(jsonMap);
        if(jsonMap == null){
            throw new IOException("Erro no parse");
        }
        try {
            this.nome = ((String) jsonMap.get("nome"));
            this.idade = ((int)jsonMap.get("idade"));
            this.email= ((String) jsonMap.get("email"));
        }
        catch (Exception e){
            throw new IOException("Json Invalido!");
        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "nome - '" + nome + '\'' +
                ", idade - " + idade +
                ", email - '" + email + '\'' +
                '}';
    }

    public String toJSON() {
        return "{" +
                "\"nome\":\"" + nome + "\"," +
                "\"idade\":" + idade + "," +
                "\"email\":\"" + email + "\"" +
                "}";
    }
}
