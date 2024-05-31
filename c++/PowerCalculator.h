#ifndef __POWERCALCULATOR_H__
#define __POWERCALCULATOR_H__

#include <iostream>
#include <thread>
#include <string>
#include <unistd.h>
#include <iostream>
#include <cstdlib>
#include <vector>


using Variable = std::pair<std::string, std::string>;
using SymbolList = std::vector<Variable>;

class PowerCalculator {
    private:
        bool error_var = false;
        std::vector<std::string> calculationsHistory;
    protected:
        void error(std::string msg){
            this->error_var = true;
            std::cout << msg << std::endl;
        }
        
    public:
        SymbolList symbolList;
        
        
        PowerCalculator();
        virtual ~PowerCalculator();

        void updateCalulationHistory(std::string expr) {
            this->calculationsHistory.push_back(expr);
        }
        void printCalulationHistory() {
            for (std::string expr : calculationsHistory){
                std::cout << expr << std::endl;
            }
        }

        bool isOperator(char op);
        bool containsDot(std::string expr);
        bool isSubSign(char sign);
        bool isAddSign(char sign);

        std::string getExprValue(std::string expr);

        std::string signAndDoubleFilter(std::string expr);

        bool containsFirstPriorityOperator(std::string expr);                                                                               
        bool containsSecondPriorityOperator(std::string expr);
        char getFirstPrioritySign(std::string expr);
        char getSecondPrioritySign(std::string expr);

        bool isNumber(std::string expr);

        std::array<std::string, 2> subsleft(std::string left);
        std::array<std::string, 2> subsright(std::string right);

        int findChar(std::string expr, char c, bool firstApparition = true, int start = 0, int end = -1);
        int getAddSignFirstIndex(std::string expr);
        int getSubSignFirstIndex(std::string expr);
        int getMulSignFirstIndex(std::string expr);
        int getDivSignFirstIndex(std::string expr);
        int getModSignFirstIndex(std::string expr);

        std::string checker(std::string expr);
        std::string doCalculation(std::string expr, char sign, int i);
        std::string reverseString(std::string str);

        std::string expressionReducer(std::string expr);

        bool hasAnyParenthesis(std::string expr);
        bool hasAnyOperator(std::string expr);
        std::string checkMulOperator(std::string expr);

        bool isIdent(std::string expr);
        bool isAssignment(std::string expr);

        std::string getAssignLeftPart(std::string expr);
        std::string getAssignRightPart(std::string expr);

        bool isVarAlreadyDefined(std::string symb);
        std::string getVarValFromSymTable(std::string symb);
        void updateVariable(std::string symb, std::string newVal);

        void lrParenthesisChecker(std::string expr);



};

#endif /* __POWERCALCULATOR_H__ */