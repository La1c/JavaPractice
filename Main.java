package org.vladimir.ageev;
import java.util.*;
import java.util.function.*;



public class Main {

    public static abstract class Sendable<T> {

        private final String to;
        private final String from;
        private final T content;

        public Sendable(String from, String to, T content){
            this.to = to;
            this.from = from;
            this.content = content;
        }

        public String getFrom(){
            return from;
        }

        public String getTo(){
            return to;
        }

        public T getContent(){
            return content;
        }
    }


    public static class MailMessage extends Sendable<String> {

        public MailMessage(String from, String to, String content){
           super(from,to,content);
        }
    }

    public static class Salary extends Sendable<Integer> {


        public Salary(String from, String to, Integer content){
                super(from,to,content);
        }
    }


    public  static class MailMap<T> extends LinkedHashMap<String, List<T>>{
        @Override
        public List<T> get(Object key){
            if(this.containsKey(key)){
                return super.get(key);
            }
            else
            {
                return Collections.<T>emptyList();
            }
        }
    }

    public static class MailService <T> implements Consumer<Sendable<T>> {
        // implement here
        private final  Map<String, List<T>> mailbox;

        public MailService(){
            mailbox = new MailMap<>();
        }

        @Override
        public void accept(Sendable<T> o) {
            if(mailbox.containsKey(o.getTo())){
                mailbox.get(o.getTo()).add(o.getContent());
            }
            else {
                mailbox.put(o.getTo(),new LinkedList<>());
                mailbox.get(o.getTo()).add(o.getContent());
            }
        }

        public Map<String, List<T>> getMailBox(){
            return mailbox;
        }
    }

    public static void main(String[] args) {
        // Random variables
        String randomFrom = "Vasia Pupkin"; // Random string
        String randomTo = "Petia Petrov";  // Random string
        int randomSalary = 100;  // Random positive whole number

// Creating list of three messages
        MailMessage firstMessage = new MailMessage(
                "Robert Howard",
                "H.P. Lovecraft",
                "This \"The Shadow over Innsmouth\" story is real masterpiece, Howard!"
        );

        assert firstMessage.getFrom().equals("Robert Howard"): "Wrong firstMessage from address";
        assert firstMessage.getTo().equals("H.P. Lovecraft"): "Wrong firstMessage to address";
        assert firstMessage.getContent().endsWith("Howard!"): "Wrong firstMessage content ending";

        MailMessage secondMessage = new MailMessage(
                "Jonathan Nolan",
                "Christopher Nolan",
                "Брат, почему все так хвалят только тебя, когда практически все сценарии написал я. Так не честно!"
        );

        MailMessage thirdMessage = new MailMessage(
                "Stephen Hawking",
                "Christopher Nolan",
                "Я так и не понял Интерстеллар."
        );

        List<MailMessage> messages = Arrays.asList(
                firstMessage, secondMessage, thirdMessage
        );

// Creating mail service
        MailService<String> mailService = new MailService<>();

// Processing of the list of messages by the mail service
        messages.stream().forEachOrdered(mailService);

//  Getting and checking "mailbox" dictionary
// recipient <-> list of messages he got
        Map<String, List<String>> mailBox = mailService.getMailBox();

        assert mailBox.get("H.P. Lovecraft").equals(
                Arrays.asList(
                        "This \"The Shadow over Innsmouth\" story is real masterpiece, Howard!"
                )
        ): "wrong mailService mailbox content (1)";

        assert mailBox.get("Christopher Nolan").equals(
                Arrays.asList(
                        "Брат, почему все так хвалят только тебя, когда практически все сценарии написал я. Так не честно!",
                        "Я так и не понял Интерстеллар."
                )
        ): "wrong mailService mailbox content (2)";

        assert mailBox.get(randomTo).equals(Collections.<String>emptyList()): "wrong mailService mailbox content (3)";


// Creating list of three salaries
        Salary salary1 = new Salary("Facebook", "Mark Zuckerberg", 1);
        Salary salary2 = new Salary("FC Barcelona", "Lionel Messi", Integer.MAX_VALUE);
        Salary salary3 = new Salary(randomFrom, randomTo, randomSalary);

// Creating service managing salaries
        MailService<Integer> salaryService = new MailService<>();

// Processing salaries
        Arrays.asList(salary1, salary2, salary3).forEach(salaryService);

//  Getting and checking "mailbox" dictionary
// recipient <-> list of salaries he got
        Map<String, List<Integer>> salaries = salaryService.getMailBox();
        assert salaries.get(salary1.getTo()).equals(Arrays.asList(1)): "wrong salaries mailbox content (1)";
        assert salaries.get(salary2.getTo()).equals(Arrays.asList(Integer.MAX_VALUE)): "wrong salaries mailbox content (2)";
        assert salaries.get(randomTo).equals(Arrays.asList(randomSalary)): "wrong salaries mailbox content (3)";
    }
}
