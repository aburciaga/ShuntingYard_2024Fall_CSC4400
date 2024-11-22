package driver;

public class ShuntngYard {
	public static String Operators = "+-*/^()";
	public static String Numbers = "1234567890";
	
	public static boolean IsNumber(String input) {
		for(char c: input.toCharArray()) {
			if (Numbers.indexOf(c) == -1) {
				return false;
			}
		}
		return true;
	}
	
	public static int getPrecedence(String input) {
		char op = input.charAt(0);
		switch (op) {
			case '(':
				return 1;
			case '+':
			case '-':
				return 2;
			case '*':
			case '/':
				return 3;
			case '^':
				return 4;
			case ')':
				return 5;
			default:
				return 0;
		}			
	}


	public static TokenList ParseFromExp(String exp) {
		TokenList lst = new TokenList(); 
		String curValue = "";
		for(char c: exp.toCharArray()) {
			if (Operators.indexOf(c) > -1) { 
				if (!curValue.isEmpty()) {
					Node<String> num = new Node(curValue);
					lst.Append(num);
					curValue = "";
				}
				Node<String> node = new Node(String.format("%c", c));
				lst.Append(node);
			} else if (Numbers.indexOf(c) > -1){ 
				curValue += c;
			}
		}
		if (curValue.length() != 0) {
			Node<String> num = new Node(curValue);
			lst.Append(num);
		}
		return lst;
	}
	
	public static TokenList BuildFromTokens(TokenList tokenList) {
		TokenList outputQueue = new TokenList();
		TokenList opStack = new TokenList();
		Node<String> token = tokenList.Head;
		tokenList.Remove(token);
		while (token != null) {
			if (IsNumber(token.Payload)) {
				outputQueue.Enqueue(token);
			} else {
				int rank = getPrecedence(token.Payload);
				if (rank == 1) { //'('
					opStack.Push(token);
				} else if (rank == 5) { //')
					Node<String> op = opStack.Peek();
					while (op != null && op.Payload != "(") {
						outputQueue.Enqueue(opStack.Pop());
						op = opStack.Peek();
					}
					opStack.Pop();
				} else {
					Node<String> op = opStack.Peek();
					while (op != null) {
						int newRank = getPrecedence(op.Payload);
						if (newRank > rank) {
							outputQueue.Enqueue(opStack.Pop());
							op = opStack.Peek();
						} else {
							break;
						}
					}
					opStack.Push(token);
				}
			}
			token = tokenList.Head;
			if (token != null) {
				tokenList.Remove(token);
			}
		}
		
		
		return outputQueue;
	}
	

	public static int Process(TokenList queue) {
		TokenList stack = new TokenList();
		while (!queue.IsEmpty()) {
			Node<String> token = queue.Dequeue();
			if (IsNumber(token.Payload)) {
				stack.Push(token);
			} else {
				Node<String> firstRight = stack.Pop();
				Node<String> secondLeft = stack.Pop();
				int result = Math(secondLeft.Payload, firstRight.Payload, token.Payload);
				Node<String> newNode = new Node<String>(result + "");
				stack.Push(newNode);
			}
		}
		return Integer.parseInt(stack.Pop().Payload);
	}
	
	public static int Math(String leftNumber, String rightNumber, String Op) {
		switch (Op) {
			case "+":
				return Integer.parseInt(leftNumber) + Integer.parseInt(rightNumber);
			case "-":
				return Integer.parseInt(leftNumber) - Integer.parseInt(rightNumber);
			case "*":
				return Integer.parseInt(leftNumber) * Integer.parseInt(rightNumber);
			case "/":
				return Integer.parseInt(leftNumber) / Integer.parseInt(rightNumber);
			case "^":
			default:
					return 0;
		}
	}
}
