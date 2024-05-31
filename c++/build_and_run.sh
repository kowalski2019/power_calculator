#!/bin/bash

g++ -o main main.c++ PowerCalculator.c++
[ $? -eq 0 ] && [ -f "main" ] && ./main