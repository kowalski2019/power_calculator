#include "PowerCalculator.hpp"

PowerCalculator::PowerCalculator()
{
    this->symbolList = SymbolList();
}
PowerCalculator::~PowerCalculator()
{
}

bool PowerCalculator::isOperator(char op)
{
    return this->isAddSign(op) || this->isSubSign(op) || this->isMulSign(op) || this->isDivSign(op) || this->isModSign(op);
}

int PowerCalculator::findChar(std::string expr, char c, bool firstApparition /*= true*/, int start /* = 0*/, int end /* = -1*/)
{
    int _end = end == -1 ? expr.size() : end;
    int res = -1;
    if (start > _end)
        return res;
    for (int i = start; i < _end; ++i)
    {
        if (expr.at(i) == c)
        {
            res = i;
            if (firstApparition)
                break;
        }
    }
    return res;
}

bool PowerCalculator::containsDot(std::string expr)
{
    return expr.find('.') != -1;
}

bool PowerCalculator::isSubSign(char sign)
{
    return sign == '-';
}
bool PowerCalculator::isAddSign(char sign)
{
    return sign == '+';
}
bool PowerCalculator::isMulSign(char sign)
{
    return sign == '*' || sign == 'x';
}
bool PowerCalculator::isDivSign(char sign)
{
    return sign == '/';
}
bool PowerCalculator::isModSign(char sign)
{
    return sign == '%';
}

int PowerCalculator::getAddSignFirstIndex(std::string expr)
{
    std::string::size_type res = expr.find('+');
    if (res == std::string::npos)
    {
        /*error , should never occur during a calculation*/
    }
    return res;
}

int PowerCalculator::getSubSignFirstIndex(std::string expr)
{
    std::string::size_type res = expr.find('-');
    if (res == std::string::npos)
    {
        /*error should never occur during a calculation*/
    }
    return res;
}

int PowerCalculator::getMulSignFirstIndex(std::string expr)
{
    std::string::size_type res = expr.find('*');
    if (res == std::string::npos)
    {
        /*error should never occur during a calculation*/
    }
    return res;
}

int PowerCalculator::getDivSignFirstIndex(std::string expr)
{
    std::string::size_type res = expr.find('/');
    if (res == std::string::npos)
    {
        /*error should never occur during a calculation*/
    }
    return res;
}

int PowerCalculator::getModSignFirstIndex(std::string expr)
{
    std::string::size_type res = expr.find('%');
    if (res == std::string::npos)
    {
        /*error should never occur during a calculation*/
    }
    return res;
}

bool PowerCalculator::containsFirstPriorityOperator(std::string expr)
{
    return (findChar(expr, '*') + findChar(expr, '/') + findChar(expr, '%')) > -3;
}

bool PowerCalculator::containsSecondPriorityOperator(std::string expr)
{
    return (findChar(expr, '+') + findChar(expr, '-')) > -2;
}

char PowerCalculator::getFirstPrioritySign(std::string expr)
{
    std::string::size_type res_mul = expr.find_first_of('*');
    std::string::size_type res_div = expr.find_first_of('/');
    std::string::size_type res_mod = expr.find_first_of('%');
    int res = res_mul;
    for (auto i : {res_div, res_mod})
    {
        res = i < res ? i : res;
    }
    return expr.at(res);
}

char PowerCalculator::getSecondPrioritySign(std::string expr)
{
    std::string::size_type res_add = expr.find_first_of('+');
    std::string::size_type res_sub = expr.find_first_of('-');
    return res_add < res_sub ? expr.at(res_add) : expr.at(res_sub);
}

bool PowerCalculator::isNumber(std::string expr)
{
    if (expr.empty())
        return false;

    int check = 0;
    if (expr.at(0) == '-' || expr.at(0) == '+')
    {
        if (expr.size() == 1)
            return false;
        else
        {
            expr = expr.substr(1);
        }
    }

    for (char c : expr)
    {
        if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9')
        {
            check += 1;
        }
    }

    return check == expr.size();
}

std::string PowerCalculator::reverseString(std::string str)
{
    std::string res;
    std::copy(str.rbegin(), str.rend(), std::back_inserter(res));
    return res;
}


 /**
  * the function split from the left part an expression the first number-literal
  * that it finds and return a 2 dimension String-array
  * where the index 0 contains the rest of the left part
  * and the index 1 the first number-literal
  * egg: 12.34+56-78*90  should be return
  *  result[0] = 12.34+56-78*
  *  result[1] = 90
  */
std::array<std::string, 2> PowerCalculator::splitLeftNumberLiteral(std::string left)
{
    std::array<std::string, 2> res;
    int leftOpOffset = 0;
    std::string num;
    for (int i = left.size() - 1; i >= 0; i--)
    {
        if (isOperator(left.at(i)))
        {
            leftOpOffset = i + 1;
            break;
        }
        std::string str("");
        str += left.at(i);
        if (isNumber(str))
        {
            num += str;
        }
    }
    res[0] = left.substr(0, leftOpOffset);
    res[1] = reverseString(num);
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
std::array<std::string, 2> PowerCalculator::splitRightNumberLiteral(std::string right)
{
    std::array<std::string, 2> res;
    int rightOpOffset = right.size();
    int i = 0;
    std::string num;
    if (right.at(0) == '-')
    {
        num += '-';
    }
    else if (right.at(0) == '+')
    {
        i += 1;
    }
    for (; i < right.size(); i++)
    {
        if (i > 0 && isOperator(right.at(i)))
        {
            rightOpOffset = i;
            break;
        }

        std::string str("");
        str += right.at(i);
        if (isNumber(str))
        {
            num += str;
        }
    }

    res[0] = right.substr(rightOpOffset);
    res[1] = num;

    return res;
}

std::string PowerCalculator::simplifyOperatorConcatenation(std::string expr)
{
    for (int i = 0; i < expr.size(); i++)
    {
        if (i + 1 <= expr.size())
        {
            if (isAddSign(expr.at(i)) && isSubSign(expr.at(i + 1)))
            {
                expr = expr.substr(0, i) + expr.substr(i + 1);
            }
            if (isAddSign(expr.at(i)) && isAddSign(expr.at(i + 1)))
            {
                expr = expr.substr(0, i) + expr.substr(i + 1);
            }
        }
        if (i + 1 <= expr.size() && i + 2 <= expr.size())
        {
            if (isSubSign(expr.at(i)) && isAddSign(expr.at(i + 1)))
            {
                expr = expr.substr(0, i) + "-" + expr.substr(i + 2);
            }
            if (isSubSign(expr.at(i)) && isSubSign(expr.at(i + 1)))
            {
                expr = expr.substr(0, i) + "+" + expr.substr(i + 2);
            }
        }
    }

    return expr;
}

bool PowerCalculator::checkOperatorConcatenation(std::string expr)
{
    bool res = false;
    int count = 0;
    for (int i = 0; i < expr.size(); i++)
    {
        if (isSubSign(expr.at(i)) || isAddSign(expr.at(i)))
        {
            count += 1;
        } else 
        {
            count = 0;
        }
        if (count >= 2) {
            res = true;
            break;
        }
    }

    return res;
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
std::string PowerCalculator::doCalculation(std::string expr, char sign, int i)
{
    std::string left, right, valString, result;
    double valDouble = 0;
    int valInteger = 0;
    bool isResInteger = false;
    int operatorIndex,operatorIndexPlusOne;
    operatorIndex=i; operatorIndexPlusOne=i+1;
    std::array<std::string, 2> subsleftRes = this->splitLeftNumberLiteral(expr.substr(0, operatorIndex));
    std::array<std::string, 2> subsrightRes = this->splitRightNumberLiteral(expr.substr(operatorIndexPlusOne));

    left = subsleftRes[1];
    right = subsrightRes[1];
    if (this->containsDot(left) && this->containsDot(right)) 
    {
        switch (sign)
        {
            case '+': valDouble = std::stod(left) + std::stod(right);
                break;
            case '-': valDouble = std::stod(left) - std::stod(right);
                break;
            case '*': valDouble = std::stod(left) * std::stod(right);
                break;
            case 'x': valDouble = std::stod(left) * std::stod(right);
                break;
            case '/': valDouble = std::stod(left) / std::stod(right);
                break;
            case '%': valDouble = std::stoi(left) % std::stoi(right);
                break;
            default: this->error("Unknow sign " + sign);
                break;
        }
        
    } 
    else if (!this->containsDot(left) && !this->containsDot(right)) 
    {
        isResInteger = true;
        switch (sign)
        {
            case '+': valInteger = std::stoi(left) + std::stoi(right);
                break;
            case '-': valInteger = std::stoi(left) - std::stoi(right);
                break;
            case '*': valInteger = std::stoi(left) * std::stoi(right);
                break;
            case 'x': valInteger = std::stoi(left) * std::stoi(right);
                break;
            case '/': valInteger = std::stoi(left) / std::stoi(right);
                break;
            case '%': valInteger = std::stoi(left) % std::stoi(right);
                break;
            default: this->error("Unknow sign " + sign);
                break;
        }

    } 
    else if (this->containsDot(left) && !this->containsDot(right)) 
    {
        switch (sign)
        {
            case '+': valDouble = std::stod(left) + std::stoi(right);
                break;
            case '-': valDouble = std::stod(left) - std::stoi(right);
                break;
            case '*': valDouble = std::stod(left) * std::stoi(right);
                break;
            case 'x': valDouble = std::stod(left) * std::stoi(right);
                break;
            case '/': valDouble = std::stod(left) / std::stoi(right);
                break;
            case '%': valDouble = std::stoi(left) % std::stoi(right);
                break;
            default: this->error("Unknow sign " + sign);
                break;
        }
    } 
    else if (!this->containsDot(left) && this->containsDot(right))
    {
        switch (sign)
        {
            case '+': valDouble = std::stoi(left) + std::stod(right);
                break;
            case '-': valDouble = std::stoi(left) - std::stod(right);
                break;
            case '*': valDouble = std::stoi(left) * std::stod(right);
                break;
            case 'x': valDouble = std::stoi(left) * std::stod(right);
                break;
            case '/': valDouble = std::stoi(left) / std::stod(right);
                break;
            case '%': valDouble = std::stoi(left) % std::stoi(right);
                break;
            default: this->error("Unknow sign " + sign);
                break;
        }
    } 
    else {
        /* nothing to do */
    }
    valString = std::to_string(isResInteger ? valInteger : valDouble);

    if (valString.length() > 0) {
        if (valString.at(0) != '-') valString = '+' + valString;
    }
    result = subsleftRes[0] + valString + subsrightRes[0];
    std::cout << "left: " <<subsleftRes[0] << " | val: "<< valInteger << " | right: "<< subsrightRes[0] << " | isResInteger: " << isResInteger << std::endl;

    return result;
}
