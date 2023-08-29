import java.io.IOException;
import java.util.List;

public class PersonService {
    public static String fromListToJsonArray(List<Person> list) {
        String str = "";
        str += "[";
        for (Person person : list) {
            str += person.toJSON();
            str += ",";
        }

        str += "]";
        return str;
    }

    public static boolean personExists(Person person) {
        try {
            resgatarPessoaPorNome(person.getNome());
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static void salvarPessoa(Person person) throws IOException {
        List<Person> people = FileDatabase.resgatarTodasPessoas();
        people.add(person);
        try {
            FileDatabase.sobrescreverListaPessoa(people);
        } catch (IOException e) {
            throw new IOException("Nao foi possivel salvar a pessoa");
        }

    }

    public static Person resgatarPessoaPorNome(String nome) throws IOException {
        List<Person> pessoas = FileDatabase.resgatarTodasPessoas();


        for (Person person : pessoas) {
            if (person.getNome().equalsIgnoreCase(nome)) {
                return person;
            }
        }
        throw new IOException("Nenhuma pessoa com esse nome");
    }

    public static void editarPessoa(String nomeIdentificador, StringBuilder novoCorpoPessoa) throws IOException {
        try {
            Person pessoa = resgatarPessoaPorNome(nomeIdentificador);
            Person pessoaNova = new Person(novoCorpoPessoa.toString());
            pessoaNova.setNome(pessoa.getNome());

            List<Person> pessoas = FileDatabase.resgatarTodasPessoas();
            for (int i = 0; i < pessoas.size(); i++) {
                if (pessoas.get(i).getNome().equalsIgnoreCase(nomeIdentificador)) {
                    pessoas.set(i, pessoaNova);
                    break;
                }
            }
            System.out.println(pessoas);
            FileDatabase.sobrescreverListaPessoa(pessoas);
        } catch (IOException e) {
            throw new IOException("Não foi possível editar essa pessoa");
        }
    }
}
