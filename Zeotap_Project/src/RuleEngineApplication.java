import java.util.HashMap;
import java.util.Map;

// AST Node Structure
class Node {
    String type;
    Node left, right;
    Object value;

    public Node(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public Node(String type, Node left, Node right) {
        this.type = type;
        this.left = left;
        this.right = right;
    }

    // New constructor to handle type, value, left, and right nodes
    public Node(String type, Object value, Node left, Node right) {
        this.type = type;
        this.value = value;
        this.left = left;
        this.right = right;
    }
}

// Rule Engine for user eligibility
public class RuleEngineApplication {

    // Parse rule string into AST (mock parser for demonstration)
    public Node createRule(String ruleString) {
        // For demonstration, manually creating a simple AST for a rule like:
        // "(age > 30 AND department = 'Sales')"
        Node ageCondition = new Node("operand", ">");  // age > 30
        ageCondition.left = new Node("attribute", "age");
        ageCondition.right = new Node("value", 30);

        Node departmentCondition = new Node("operand", "=");  // department = 'Sales'
        departmentCondition.left = new Node("attribute", "department");
        departmentCondition.right = new Node("value", "Sales");

        // AND condition combining age and department conditions
        return new Node("operator", "AND", ageCondition, departmentCondition);
    }

    // Combine rules into a single AST
    public Node combineRules(Node rule1, Node rule2) {
        // Combines two rules using an AND operator
        return new Node("operator", "AND", rule1, rule2);
    }

    // Evaluate rule on provided data
    public boolean evaluateRule(Node node, Map<String, Object> data) {
        if (node == null) return true;

        // Check if the node is an operator (AND / OR)
        if ("operator".equals(node.type)) {
            if ("AND".equals(node.value)) {
                return evaluateRule(node.left, data) && evaluateRule(node.right, data);
            } else if ("OR".equals(node.value)) {
                return evaluateRule(node.left, data) || evaluateRule(node.right, data);
            }
        }

        // Handle operand for comparison
        else if ("operand".equals(node.type)) {
            if (node.left == null || node.right == null) return false;  // Null check

            // Attribute and comparison value
            Object attributeValue = data.get(node.left.value);
            Object comparisonValue = node.right.value;

            if (attributeValue == null) return false;  // Attribute not found in data

            // Perform comparison based on operand
            if (">".equals(node.value) && attributeValue instanceof Integer && comparisonValue instanceof Integer) {
                return (int) attributeValue > (int) comparisonValue;
            } else if ("<".equals(node.value) && attributeValue instanceof Integer && comparisonValue instanceof Integer) {
                return (int) attributeValue < (int) comparisonValue;
            } else if ("=".equals(node.value)) {
                return attributeValue.equals(comparisonValue);
            }
        }
        return false;
    }

    public static void main(String[] args) {
        RuleEngineApplication ruleEngine = new RuleEngineApplication();
        
        // Create a rule AST for demonstration purposes
        String rule1 = "(age > 30 AND department = 'Sales')";
        Node ruleAST = ruleEngine.createRule(rule1);
        
        // Sample user data using HashMap instead of Map.of()
        Map<String, Object> userData = new HashMap<>();
        userData.put("age", 35);
        userData.put("department", "Sales");
        userData.put("salary", 60000);
        userData.put("experience", 3);

        // Evaluate rule based on user data
        boolean eligibility = ruleEngine.evaluateRule(ruleAST, userData);
        System.out.println("User Eligibility: " + eligibility);  // Expected output: true
    }
}
