import java.util.*;

class PowerCalculator2{

  static boolean error = false;
  static  List<Variable<String,String>> symbolList = new ArrayList<>();

  /**
   * getValue evalutes an expression like "1+2-3*4/5%6"
   * the function can't evalutes expressions like "(1+2)*3"
   *
   * @param expr
   * @return expr
   */
  static String getValue(String expr){

    expr = checker(expr);
    if(isIdent(expr)) return getVarValFromSymTable(expr);
    while(haveAnyOp(expr)){

          while(containsFirstPriority(expr)){
            char sign = giveFirstPrioritySign(expr);
            if(sign == '*' && (expr.length() > 2)){
              expr = parseCalc(expr, sign, parse_mul(expr));
              expr = checker(expr);

            }
            if(sign == '/' && (expr.length() > 2)){
              expr = parseCalc(expr, sign, parse_div(expr));
              expr = checker(expr);

            }

            if(sign == '%' && (expr.length() > 2)){
              expr = parseCalc(expr, sign, parse_mod(expr));
              expr = checker(expr);

            }
          }

          while(containsSecondPriority(expr)){
           char sign1 = giveSecondPrioritySign(expr);
            if(sign1 == '+' && (expr.length() > 2)){
              expr = parseCalc(expr,sign1,parse_add(expr));
              expr = checker(expr);

            }
            if(sign1 == '-' && (expr.length() > 2)){
              expr = parseCalc(expr, sign1, parse_sub(expr));
              expr = checker(expr);

            }
          }

      }


      return signAndDoubleFilter(expr);
  }

  /**
   *
   */
  static String signAndDoubleFilter(String expr){
    if(expr.charAt(0) == '+') expr = expr.substring(1, expr.length());
    if(containsDot(expr)){
      if(expr.substring(expr.indexOf('.') + 1).equals("0"))
        return expr.substring(0, expr.indexOf('.'));
    }
    return expr;
  }

  /**
   * containsFirstPriority is able to find any binary operator in an expression with
   * high priority egg: " * ", " / ", " % "
   * the function return true when the expression contains one else false
   */
 static boolean containsFirstPriority(String expr){
   int tester = 0;
   for(int i = 1; i < expr.length(); i++){
     if((expr.charAt(i) == '*' || expr.charAt(i) == '/' || expr.charAt(i) == '%') && i > 0)
      tester++;
   }
   return tester != 0;
 }

  /**
   * containsSecondPriority is able to find any binary operator in an expression with
   * low priority egg: " + ", " - "
   * the function return true when the expression contains one else false
   */
 static boolean containsSecondPriority(String expr){
  int tester = 0;
  for(int i = 1; i < expr.length(); i++){
    if((expr.charAt(i) == '+' || expr.charAt(i) == '-') && i > 0)
      tester++;
  }
  return tester != 0;
 }

 /**
  * giveFirstPrioritySign looks for from left to right of an expression
  * the first binary operator with high priority and return that
  */
static char giveFirstPrioritySign(String expr){
  char res = '\0';
  for(int i = 0; i < expr.length(); i++){
    if((expr.charAt(i) == '*' || expr.charAt(i) == '/' || expr.charAt(i) == '%') && i > 0){
        res = expr.charAt(i);
        break;
    }
  }
  return res;
}


/**
  * giveFirstSecondSign looks for from left to right of an expression
  * the first binary operator with low priority and return that
  */
static char giveSecondPrioritySign(String expr){
    char res = '\0';
    for(int i = 0; i < expr.length(); i++){
      if((expr.charAt(i) == '+' || expr.charAt(i) == '-') && i > 0){
        res=expr.charAt(i);
        break;
    }
  }
  return res;
}

/**
 * isOp check if a character is a binray operator and return true else false
 * @param op
 * @return
 */
 static boolean isOp(char op){
    if(op == '+' || op == '-' ||  op == '*' || op == '/' || op == '%')
      return true;
    return false;
 }


static boolean containsDot(String expr){
    for(int i = 0; i < expr.length(); i++)
      if(expr.charAt(i) == '.')
        return true;
    return false;
 }

 /**
  * the function split from the left part an expression the first Int-literal
  * that it finds and return a 2 dimension String-array
  * where the index 0 contains the rest of the left part
  * and the index 1 the first Int-literal
  * egg: 12.34+56-78*90  should be return
  *  result[0] = 12.34+56-78*
  *  result[1] = 90
  */
static String[] subsleft(String left){
    int c = 0;
    String result = "";
    String[] res = new String[2];
    for(int i = left.length() - 1; i >= 0; i--){
      if(isSubSign(left.charAt(i))){
        result += '-';
        c = i;
        break;
      }

      if(isOp(left.charAt(i))){
         c = i + 1;
         break;
        }

      if(isInteger(Character.toString(left.charAt(i))) || isIdent(Character.toString(left.charAt(i)))){
         result += left.charAt(i);
       }
    }
    res[0] = left.substring(0,c);
    res[1] = reverseString(result);
    return res;
  }

  /**
  * the function split from the right part an expression the first Int-literal
  * that it finds and return a 2 dimension String-array
  * where the index 0 contains the rest of the right part
  * and the index 1 the first Int-literal
  * egg: 12.34+56-78*90  should be return
  *  result[0] = +56-78*90
  *  result[1] = 12.34
  */
  static String[] subsright(String right){
    int i = 0;
    int c = right.length();
    String result = "";
    String[] res = new String[2];
    if(right.charAt(0) =='-') result += '-';
    if(right.charAt(0) =='+') i++;
    for(i = i; i < right.length(); i++){
      if(i > 0 && isOp(right.charAt(i))){
        c = i;
        break;
      }

      if(isInteger(Character.toString(right.charAt(i))) || isIdent(Character.toString(right.charAt(i)))){
        result += right.charAt(i);
      }
    }
    res[0] = right.substring(c);
    res[1] = result;
    return res;
  }


  static boolean isSubSign(char sign){
    return sign == '-';
  }

  static boolean isAddSign(char sign){
    return sign == '-';
  }



 /**
  * parse_mul return from
  * @param expr
  * @return the position of " * "
  */
  static int parse_mul(String expr){
    int res = 0;
    for(int i = 0; i < expr.length(); i++){
      if(expr.charAt(i) == '*' && i > 0){
        if(!isOp(expr.charAt(i-1)))
          res = i;
        break;
       }
     }
    return res;
  }

  /**
   *
   * @param expr
   * @return the position of " / "
   */
  static int parse_div(String expr){
    int res = 0;
    for(int i = 0; i < expr.length(); i++){
      if(expr.charAt(i) == '/' && i > 0){
        if(!isOp(expr.charAt(i-1)))
          res = i;
        break;
      }
    }
     return res;
  }

  /**
   *
   * @param expr
   * @return the position of " % "
   */
  static int parse_mod(String expr){
    int res = 0;
    for(int i = 0; i < expr.length(); i++){
      if(expr.charAt(i) == '%' && i > 0){
        if(!isOp(expr.charAt(i-1)))
          res = i;
        break;
       }
     }
     return res;
  }

  /**
   *
   * @param expr
   * @return the position of " + "
   */
  static int parse_add(String expr){
    int res = 0;
    for(int i = 0; i < expr.length(); i++){
      if(expr.charAt(i) == '+' && i > 0){
        if(!isOp(expr.charAt(i-1)))
          res = i;
        break;
       }
     }
     return res;
  }

  /**
   *
   * @param expr
   * @return the position of " - "
   */
  static int parse_sub(String expr){
    int res = 0;
    for(int i = 0; i < expr.length(); i++){
      if(expr.charAt(i) == '-' && i > 0){
        if(!isOp(expr.charAt(i-1)))
          res = i;
        break;
       }
     }
     return res;
  }

/**
 * End binary operator parser
 */

/*
  static String removeIfThree(String expr){
    int tester=0;
    for(int i=0; i<expr.length();i++){
      if(expr.charAt(i)=='+' && i<expr.length()-2) {
        for(int j=i;j<(i+2);j++){
          if(expr.charAt(j)=='+') tester++;
        }
      }
      if(tester>1){
        expr=expr.substring(0,i)+expr.substring(i+1);
        tester=0;
      }
      tester=0;
    }
    return expr;
  }
  */

  /**
   * checker works like a filter and reducer
   *  it applies the rule of binary unary in order to reduce the conflicts
   * @param expr
   * @return expr
   */
  static String checker(String expr){
    for(int i = 0; i < expr.length(); i++){
      if(expr.charAt(i)=='+' && expr.charAt(i+1)=='-'){
        expr=expr.substring(0,i)+expr.substring(i+1);
      }
      if(expr.charAt(i)=='-' && expr.charAt(i+1)=='+' && (i+1)<=expr.length() && (i+2)<=expr.length()){
        expr=expr.substring(0,i)+'-'+expr.substring(i+2);
      }
      if(expr.charAt(i)=='+' && expr.charAt(i+1)=='+'){
        expr=expr.substring(0,i)+expr.substring(i+1);
      }
      if(expr.charAt(i)=='-' && expr.charAt(i+1)=='-' && (i+1)<=expr.length() && (i+2)<=expr.length()){
        expr=expr.substring(0,i)+'+'+expr.substring(i+2);
      }
    }
    return expr;
  }

/**
 * with the help of subsleft() and subsright
 * parseCalc can evaluates 2 Int-literal
 *
 * egg : expr = 1+2-3*4/5%6
 *       sign = '*'
 *        i   = 5
 *
 * after the help of subsleft() and subsright -> left = -3  and right = 4
 * finaly after the evaluation expr = 1+2-12/5%6
 *
 * @param expr the expression self
 * @param sign the binary operator
 * @param i index of the binary operator in the expression
 * @return reduced expr
 */
  static String parseCalc(String expr,char sign,int i){

    String left,right,valS,result;
    left=right=valS=result="";
    double valD=0;
    int valI=0;
    boolean boolI=false;
    boolean boolD=false;
    int a,z; a=z=0;
    a=i; z=i+1;
       left=subsleft(expr.substring(0,a))[1];
       right=subsright(expr.substring(z))[1];
       if(isIdent(left)){
        if(isVarAlreadyDefined(left)){
           left=getVarValFromSymTable(left);
        } else error("Unknown variable '"+left+"' \n");
       }

       if(isIdent(right)){
         if(isVarAlreadyDefined(right)){
          right=getVarValFromSymTable(right);
         }else error("Unknown variable '"+right+"' \n");;
       }

    if(sign=='+') {
      if(containsDot(left) && containsDot(right)) {
        valD=Double.valueOf(left)+Double.valueOf(right);
        boolD=true;
      }
      if(!containsDot(left) && !containsDot(right)) {
        valI=Integer.valueOf(left)+Integer.valueOf(right);
        boolI=true;
      }
      if(containsDot(left) && !containsDot(right)) {
        valD=Double.valueOf(left)+Integer.valueOf(right);
        boolD=true;
      }
      if(!containsDot(left) && containsDot(right)){
        valD=Integer.valueOf(left)+Double.valueOf(right);
        boolD=true;
      }
      if(boolD) valS=Double.toString(valD);
      if(boolI) valS=Integer.toString(valI);

      if(valS.length()>0){
        if(valS.charAt(0)!='-') valS='+'+valS;
        }
      result=subsleft(expr.substring(0,a))[0]+valS+subsright(expr.substring(z))[0];
      if(result.charAt(0)=='+') result=result.substring(1);
      return result;
    }
    if(sign=='-') {
       if(containsDot(left) && containsDot(right)) {
        valD=Double.valueOf(left)-Double.valueOf(right);
        boolD=true;
      }
      if(!containsDot(left) && !containsDot(right)) {
        valI=Integer.valueOf(left)-Integer.valueOf(right);
        boolI=true;
      }
      if(containsDot(left) && !containsDot(right)) {
        valD=Double.valueOf(left)-Integer.valueOf(right);
        boolD=true;
      }
      if(!containsDot(left) && containsDot(right)){
        valD=Integer.valueOf(left)-Double.valueOf(right);
        boolD=true;
      }
      if(boolD) valS=Double.toString(valD);
      if(boolI) valS=Integer.toString(valI);

      if(valS.length()>0){
        if(valS.charAt(0)!='-') valS='+'+valS;
        }
      result=subsleft(expr.substring(0,a))[0]+valS+subsright(expr.substring(z))[0];
      if(result.charAt(0)=='+') result=result.substring(1);
      return result;
    }
    if(sign=='*') {
      if(containsDot(left) && containsDot(right)) {
        valD=Double.valueOf(left)*Double.valueOf(right);
        boolD=true;
      }
      if(!containsDot(left) && !containsDot(right)) {
        valI=Integer.valueOf(left)*Integer.valueOf(right);
        boolI=true;
      }
      if(containsDot(left) && !containsDot(right)) {
        valD=Double.valueOf(left)*Integer.valueOf(right);
        boolD=true;
      }
      if(!containsDot(left) && containsDot(right)){
        valD=Integer.valueOf(left)*Double.valueOf(right);
        boolD=true;
      }
      if(boolD) valS=Double.toString(valD);
      if(boolI) valS=Integer.toString(valI);

      if(valS.length()>0){
        if(valS.charAt(0)!='-') valS='+'+valS;
        }
      result=subsleft(expr.substring(0,a))[0]+valS+subsright(expr.substring(z))[0];
      if(result.charAt(0)=='+') result=result.substring(1);
      return result;
    }
    if(sign=='/') {
       if(containsDot(left) && containsDot(right)) {
        valD=Double.valueOf(left)/Double.valueOf(right);
        boolD=true;
      }
      if(!containsDot(left) && !containsDot(right)) {
        valI=Integer.valueOf(left)/Integer.valueOf(right);
        boolI=true;
      }
      if(containsDot(left) && !containsDot(right)) {
        valD=Double.valueOf(left)/Integer.valueOf(right);
        boolD=true;
      }
      if(!containsDot(left) && containsDot(right)){
        valD=Integer.valueOf(left)/Double.valueOf(right);
        boolD=true;
      }
      if(boolD) valS=Double.toString(valD);
      if(boolI) valS=Integer.toString(valI);

      if(valS.length()>0){
        if(valS.charAt(0)!='-') valS='+'+valS;
        }
      result=subsleft(expr.substring(0,a))[0]+valS+subsright(expr.substring(z))[0];
      if(result.charAt(0)=='+') result=result.substring(1);
      return result;
    }

    // modulo

      if(containsDot(left) && containsDot(right)) {
        valD=Double.valueOf(left)%Double.valueOf(right);
        boolD=true;
      }
      if(!containsDot(left) && !containsDot(right)) {
        valI=Integer.valueOf(left)%Integer.valueOf(right);
        boolI=true;
      }
      if(containsDot(left) && !containsDot(right)) {
        valD=Double.valueOf(left)%Integer.valueOf(right);
        boolD=true;
      }
      if(!containsDot(left) && containsDot(right)){
        valD=Integer.valueOf(left)%Double.valueOf(right);
        boolD=true;
      }
      if(boolD) valS=Double.toString(valD);
      if(boolI) valS=Integer.toString(valI);

     if(valS.length()>0){
     if(valS.charAt(0)!='-') valS='+'+valS;
     }
         result=subsleft(expr.substring(0,a))[0]+valS+subsright(expr.substring(z))[0];
         if(result.charAt(0)=='+') result=result.substring(1);
         return result;
  }

  /**
   * egg : 234 should be return " 432 "
   * @param str
   * @return str^-1
   */
  static String reverseString(String str){
    String result="";
    for(int i=str.length()-1; i>=0; i--) result+=str.charAt(i);
    return result;
  }

  /**
   * check if an Int-literal is a binray number
   * @param b
   * @return
   */
  static boolean isBin(String b){
    int dot,test;
    test=dot=0;
    for(int i=0; i<b.length();i++){
        if(b.charAt(i)=='.') {
          dot++; test++;
        }

        if((b.charAt(i)=='0' || b.charAt(i)=='1') && b.charAt(i)!='.') test++;
    }
     if(dot>1) error("wrong number format");

     return test==b.length();
}

/**
 * check if an Int-literal is an octal number
 * @param o
 * @return
 */
static boolean isOct(String o){
    int dot,test;
    dot=test=0;
    for(int i=0; i<o.length();i++){
        if(o.charAt(i)=='.') {
          dot++;
          test++;
        }
        if((o.charAt(i)=='0' || o.charAt(i)=='1' || o.charAt(i)=='2' || o.charAt(i)=='3' || o.charAt(i)=='4' || o.charAt(i)=='5' || o.charAt(i)=='6' || o.charAt(i)=='7') && o.charAt(i)!='.') test++;
      }
      if(dot>1) error("wrong number format");
      return test==o.length();
}

/**
 * check if an Int-literal is an decimal number
 * @param a
 * @return
 */
static boolean isInteger(String a) {
  int dot,test;
  dot=test=0;
    int b=0;
    if((a.charAt(0)=='-' || a.charAt(0)=='+')&& a.length()==1) return false;
    if((a.charAt(0)=='-' || a.charAt(0)=='+')&& a.length()>1){
        test++;
        b++;
    }
    for(int i=b; i<a.length();i++){
      if(a.charAt(i)=='.') {
        dot++;
        test++;
      }
        if((isOct(Character.toString(a.charAt(i))) || a.charAt(i)=='8' || a.charAt(i)=='9') && a.charAt(i)!='.') test++;
    }
    if(dot>1) error("wrong number format");
    return test==a.length();
}

/**
 * expressFilter is a recursive function, it will be called as long as that the
 * expression contains any parentheses
 * every parenthesis's content will be replaced by the result of getValue(expr) and etc...
 * @param expr
 * @return getValue(expr)
 */
static String expressFilter(String expr){

    int a,z,fp,sp;
    a=z=fp=sp=0;
    for(int i=0; i<expr.length(); i++){
        if(expr.charAt(i)=='('){
            fp++;
            a=i;
        }

        if(expr.charAt(i)==')' && fp!=0){
            sp++;
          if(sp==1) {
            z=i;
            expr=expr.substring(0,a)+getValue(expr.substring(a+1,z))+expr.substring(z+1);
            //System.out.println(expr);
            sp=0;
            fp=0;
          }
        }

    }
      if(haveAnyP(expr)) return expressFilter(expr);

      return getValue(expr);
  }


/**
 * check if an expression contains any parentheses
 */
static boolean haveAnyP(String s){
      for(int i=0; i<s.length(); i++) if(s.charAt(i)=='(') return true;
      return false;
  }

  /**
   * check if a character is a parenthesis
   * @param p
   * @return
   */
  static boolean isP(char p){
    if(p=='(' || p==')') return true;
    return false;
  }

  /**
   * check if an expression contains any binary operator
   */
  static boolean haveAnyOp(String expr){
    int tester=0;
    for(int i=0; i<expr.length(); i++) if(isOp(expr.charAt(i)) && i>0) tester++;
    if(tester>=1) return true;
    return false;
  }


/**
 * checkMul use the binary unary rule like the function checker above
 * egg : (12-34)(56+78) should be return :  (12-34)*(56+78)
 * egg : (12/34)-(56+78) should be return : (12/34)-1*(56+78)
 */
static String checkMul(String expr){

  for(int i=0; i<expr.length();i++){

    if(expr.charAt(i)==')'){
      if(i+1<expr.length()){
        if(isInteger(Character.toString(expr.charAt(i+1))) || isIdent(Character.toString(expr.charAt(i+1)))) error("Malformed expression");
      }
    }

    if(expr.charAt(i)=='('){
        if(i-1>=0){
            if(isInteger(Character.toString(expr.charAt(i-1)))){
                expr=expr.substring(0,i)+'*'+expr.substring(i);
                //System.out.println(expr);
            }
            if(expr.charAt(i-1)=='-'){
              //System.out.println("check");
              expr=expr.substring(0,i)+"1*"+expr.substring(i);
            }
        }
    }

    if(i+1<expr.length()){
      if(expr.charAt(i)==')' && expr.charAt(i+1)=='('){
            expr=expr.substring(0,i+1)+'*'+expr.substring(i+1);
     }
   }
 }
    return expr;
}

static boolean isIdent(String expr){
   int tester=0;
   for(int i=0;i<expr.length(); i++){
     for(char j='a'; j<='z';j++){
        if(expr.charAt(i)==j|| expr.toUpperCase().charAt(i)==Character.toString(j).toUpperCase().charAt(0)) tester++;
     }
   }

   return tester==expr.length();
}

static boolean isAssignment(String expr){
  int i=0;
  while(expr.charAt(i)!='='){
    i++;
    if(i==expr.length()) return false;
  }
  //if(isIdent(expr.substring(0,i))) return true;
  return true;
}

static String getAssignLeftPart(String expr){
  return expr.substring(0,expr.indexOf('='));
}

static String getAssignRightPart(String expr){
  return expr.substring(expr.indexOf('=')+1);
}

static boolean isVarAlreadyDefined(String symb){
  for(Variable<String,String> var: symbolList) if(var.key.equals(symb)) return true;
  return false;
}
static String getVarValFromSymTable(String symb){
  for(Variable<String,String> var: symbolList) if(var.key.equals(symb)) return var.value;
  return "0";
}

static void replacedVarVal(String symb,String newVal){
  for(int i=0; i<symbolList.size();i++){
    if(symbolList.get(i).key.equals(symb)) symbolList.get(i).setValue(newVal);
  }
}

  static void error(String msg){
      error=true;
      System.out.println(msg);
  }

  static void lrParControl(String expr){
    int lp,rp;
    lp=rp=0;
      for(int i=0; i<expr.length(); i++){
        if(expr.charAt(i)=='(') lp++;
        if(expr.charAt(i)==')') rp++;
      }
      if(lp!=rp){
        if(lp>rp) error("')' missing");
        if(lp<rp) error("'(' missing");
      }
  }


  static void bigTests(){
    String a=" ";
    String b="7+2/2*3-3*1+1/3*2-2+2-3*2/253%2+1-3+2+32-22/56-2321+21-21/21+2+3/-432+321*432";
    String c="(23.2)-(97.32%212-2137/21.21/(12*5-(3*23.432+31/31)-3)-212)*(12+(21.978*31+87.8-21-(21.2/2/1+(121.212-121))))";
    System.out.println(expressFilter(a)+"\n"+expressFilter(b)+"\n"+expressFilter(c));
  }

  static void demo(){
    System.out.println("demo started");
    Scanner sc= new Scanner(System.in);

    while(true){
      try{
    String expr=sc.nextLine();
    lrParControl(expr);
    expr=checkMul(expr);
    if(!error){
    if(isIdent(expr)){
      if(isVarAlreadyDefined(expr)) System.out.println("\t\t"+getVarValFromSymTable(expr));
      else System.out.println("Unknown variable '"+expr+"' \n");
    }
    else if(isAssignment(expr)){
      String varr=getAssignLeftPart(expr);
      if(!isIdent(varr)) {
        error("Unauthorized variable '"+varr+"' \n");
      }
      if(!error){
      String result=expressFilter(getAssignRightPart(expr));
      if(isVarAlreadyDefined(varr)){
        replacedVarVal(varr, result);
      }
      else{
      symbolList.add(new Variable<String,String>(varr,result));
        }
      System.out.println("\t\t"+result);
      }
    }
    else System.out.println("\t\t"+expressFilter(expr));
    }
    error=false;
    }catch (Exception e){
    if(!error) System.out.println("An error occured");
  }

}
    //System.out.println("demo stopped");
}

public static void main(String[] argv){
    demo();
    }

}



class Variable<D,E> {
  D key;
  E value;
  public Variable(D key,E value){
    this.key=key;
    this.value=value;
  }
  public void setValue(E val){
    this.value=val;
  }
  @Override
  public String toString(){
    return "key : "+this.key+" ==> value : "+this.value;
  }
}
