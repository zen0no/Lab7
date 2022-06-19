package other;

public class Predicates{
    public static abstract class Predicate{

    }

    public static class Max extends Predicate{
        public String method = "max";
        String field;
        public Max(String f){
            field = f;
        };
    }

    public static class Lower extends Predicate{
        public String method = "lower";
        public String field;
        public Object value;
        public Lower(String f, Object value){
            this.field = f;
            this.value = value;
        };
    }

    public static class Greater extends Predicate{
        public String method = "greater";
        public String field;
        public Object value;
        public Greater(String f, Object value){
            this.field = f;
            this.value = value;
        };
    }

    public static class Equal extends Predicate{
        public String method = "equal";
        public String field;
        public Object value;
        public Equal(String f, Object value){
            this.field = f;
            this.value = value;
        };
    }
}
