#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <map>
#include <cmath>
#include "json.hpp" // You need to download and include nlohmann/json.hpp

using json = nlohmann::json;
using namespace std;

// Function to convert a number from a given base to decimal
long long convertToDecimal(const string& value, int base) {
    return stoll(value, nullptr, base);
}

// Function to perform Lagrange interpolation to find the constant term
long long lagrangeInterpolation(const vector<pair<int, long long>>& points) {
    long long c = 0;
    int k = points.size();
    
    for (int i = 0; i < k; i++) {
        long long term = points[i].second; // y_i
        long long num = 1, denom = 1;
        
        for (int j = 0; j < k; j++) {
            if (i != j) {
                num *= -points[j].first;
                denom *= (points[i].first - points[j].first);
            }
        }
        
        c += term * (num / denom);
    }
    
    return c;
}

int main() {
    ifstream file("testcase1.json"); // Ensure your input file is named "input.json"
    if (!file.is_open()) {
        cerr << "Error opening file!" << endl;
        return 1;
    }
    
    json input;
    file >> input;
    file.close();
    
    vector<pair<int, long long>> points;
    int k = input["keys"]["k"];
    
    for (auto& [key, value] : input.items()) {
        if (key == "keys") continue; // Skip the keys object
        
        int x = stoi(key);
        int base = stoi(value["base"].get<string>());
        long long y = convertToDecimal(value["value"].get<string>(), base);
        
        points.emplace_back(x, y);
        if (points.size() == k) break;
    }
    
    long long secret = lagrangeInterpolation(points);
    cout << "Secret: " << secret << endl;
    
    return 0;
}
