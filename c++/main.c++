#include "PowerCalculator.hpp"
#include <cstdlib>
#include <iostream>
#include <ctime>

int main(int argc, char **argv){
    PowerCalculator powCalc = PowerCalculator();
    std::string expr {"23/232*3232-23"};
    std::cout << "full number: " << expr << std::endl;
    std::cout << "left: " << powCalc.subsleft(expr)[0] << std::endl;
    std::cout << "right: "<< powCalc.subsright(expr)[0] << std::endl;

    std::cout << "isNumber: " << powCalc.isNumber("2.23") << std::endl;

    //std::cout << "checkOperatorConcatenation: "<< powCalc.doCalculation(s, '*', powCalc.findChar(s, '*')) << std::endl;
    return 0;
}
