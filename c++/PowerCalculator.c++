#include "PowerCalculator.h"

PowerCalculator::PowerCalculator()
{
    this->symbolList = SymbolList();
}
PowerCalculator::~PowerCalculator()
{

}

bool PowerCalculator::isOperator(char op){
    return op == '+' || op == '-' ||  op == '*' || op == '/' || op == '%';
}

int PowerCalculator::findChar(std::string expr, char c, bool firstApparition /*= true*/, int start /* = 0*/, int end /* = -1*/)
{
    int _end = end == -1 ? expr.size() : end;
    int res = -1;
    if (start  > _end)
        return res;
    for (int i = start ; i < _end; ++i){
       if(expr.at(i) == c) {
            res = i;
            if(firstApparition)
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

int PowerCalculator::getAddSignFirstIndex(std::string expr)
{
    std::string::size_type res = expr.find('+');
    if (res == std::string::npos) {
        /*error , should never occur during a calculation*/
    }
    return res;
}

int PowerCalculator::getSubSignFirstIndex(std::string expr)
{
    std::string::size_type res = expr.find('-');
    if (res == std::string::npos) {
        /*error should never occur during a calculation*/
    }
    return res;
}

int PowerCalculator::getMulSignFirstIndex(std::string expr)
{
     std::string::size_type res = expr.find('*');
    if (res == std::string::npos) {
        /*error should never occur during a calculation*/
    }
    return res;
}

int PowerCalculator::getDivSignFirstIndex(std::string expr)
{
    std::string::size_type res = expr.find('/');
    if (res == std::string::npos) {
        /*error should never occur during a calculation*/
    }
    return res;
}

int PowerCalculator::getModSignFirstIndex(std::string expr)
{
    std::string::size_type res = expr.find('%');
    if (res == std::string::npos) {
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
    return (findChar(expr, '+') + findChar(expr, '-') ) > -2;
}

char PowerCalculator::getFirstPrioritySign(std::string expr)
{
    std::string::size_type res_mul = expr.find_first_of('*');
    std::string::size_type res_div = expr.find_first_of('/');
    std::string::size_type res_mod = expr.find_first_of('%');
    int res = res_mul;
    for (auto i: {res_div, res_mod}){
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
    if(expr.empty())
        return false;

    int check = 0;
    if (expr.at(0) == '-' || expr.at(0) == '+'){
        if (expr.size() == 1)
            return false;
        else{
            expr = expr.substr(1);
        }
    }

    for (char c: expr) {
        if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9'){
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

std::array<std::string, 2> PowerCalculator::subsleft(std::string left)
{
    int c = 0;
    std::array<std::string, 2> res;
    std::string num;
    for (int i = left.size() - 1; i >= 0; i--) {
        if(isOperator(left.at(i))) {
            c = i + 1;
            break;
        }
        std::string str("");
        str += left.at(i);
        if (isNumber(str)) {
            num += str;         
        }
    }
    res[0] = left.substr(0, c);
    res[1] = reverseString(num);
    return res;
}

std::array<std::string, 2> PowerCalculator::subsright(std::string right)
{
    std::array<std::string, 2> res;
    return res;
}