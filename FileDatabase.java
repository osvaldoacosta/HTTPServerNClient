import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class FileDatabase {


    public static void sobrescreverListaPessoa(List<Person> people) throws IOException {
        try (FileOutputStream fos = new FileOutputStream("people.txt");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(people);
        } catch (IOException ex) {
            throw new IOException("Nao foi possivel salvar pessoa(s)");
        }
    }

    public static ArrayList<Person> resgatarTodasPessoas() {
        try (FileInputStream fis = new FileInputStream("people.txt");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (ArrayList<Person>) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erro ao resgatar todas as pessoas: " + ex.getMessage());
            return new ArrayList<>();
        }

    }




    public static void main(String[] args) throws IOException {
        Person person1 = new Person("Felipe", 21,"Felipe@gmail.com");
        Person person2 = new Person("Osvaldo", 20,"Osvaldo@gmail.com");
        Person person3 = new Person("Tarciana", 18, "Tarciana@gmail.com");
        PersonService.salvarPessoa(person1);
        PersonService.salvarPessoa(person2);
        PersonService.salvarPessoa(person3);
    }


}
