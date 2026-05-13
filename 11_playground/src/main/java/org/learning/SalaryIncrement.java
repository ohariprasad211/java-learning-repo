package org.learning;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

public class SalaryIncrement {
    public static void main(String[] args) {
        var currentCTC = 2181020;
        var s = new SalaryStructure("No Hike", currentCTC, 14);
        var s1 = new SalaryStructure("1% Hike", currentCTC*1.01, 14);
        var s2 = new SalaryStructure("2% Hike", currentCTC*1.02, 14);
        var s3 = new SalaryStructure("3% Hike", currentCTC*1.03, 14);
        var s4 = new SalaryStructure("4% Hike", currentCTC*1.04, 14);
        var s5 = new SalaryStructure("5% Hike", currentCTC*1.05, 14);
        var s6 = new SalaryStructure("6% Hike", currentCTC*1.06, 14);
        var s7 = new SalaryStructure("7% Hike", currentCTC*1.07, 14);
        var s8 = new SalaryStructure("8% Hike", currentCTC*1.08, 14);
        var s9 = new SalaryStructure("9% Hike", currentCTC*1.09, 14);
        var s10 = new SalaryStructure("10% Hike", currentCTC*1.10, 14);
        var s20 = new SalaryStructure("20% Hike", currentCTC*1.20, 14);
        var s25 = new SalaryStructure("25% Hike", currentCTC*1.25, 14);
        var s30 = new SalaryStructure("30% Hike", currentCTC*1.30, 14);

        calculateSalary(List.of(s, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s20, s25, s30));

    }

    private static void calculateSalary(List<SalaryStructure> salaries) {
        var currentTakeHome = 0d;
        for (var salaryStructure : salaries) {
            var standardDeduction = 75000;
            var employeePFPerMonth = 1800;
            var taxSlabs = new HashMap<String, Integer>();
            taxSlabs.put("0-4L", 0);
            taxSlabs.put("4L-8L", 5);
            taxSlabs.put("8L-12L", 10);
            taxSlabs.put("12L-16L", 15);
            taxSlabs.put("16L-20L", 20);
            taxSlabs.put("20L-24L", 25);
            taxSlabs.put(">24L", 30);
            var grossSalary = salaryStructure.getBasic() + salaryStructure.getHra() + salaryStructure.getAllowance()
                    + salaryStructure.getTelephone();
            var taxableIncome = grossSalary - standardDeduction;
            var taxAmount = 0;
            if (taxableIncome > 400000 && taxableIncome <= 800000) {
                taxAmount = (int) ((taxableIncome - 400001) * ((double) taxSlabs.get("4L-8L") / 100));
            } else if (taxableIncome > 800000 && taxableIncome <= 1200000) {
                taxAmount = 20000 + (int) ((taxableIncome - 800001) * ((double) taxSlabs.get("8L-12L") / 100));
            } else if (taxableIncome > 1200000 && taxableIncome <= 1600000) {
                taxAmount = 60000 + (int) ((taxableIncome - 1200001) * ((double) taxSlabs.get("12L-16L") / 100));
            } else if (taxableIncome > 1600000 && taxableIncome <= 2000000) {
                taxAmount = 120000 + (int) ((taxableIncome - 1600001) * ((double) taxSlabs.get("16L-20L") / 100));
            } else if (taxableIncome > 2000000 && taxableIncome <= 2400000) {
                taxAmount = 200000 + (int) ((taxableIncome - 2000001) * ((double) taxSlabs.get("20L-24L") / 100));
            } else {
                taxAmount = 300000 + (int) ((taxableIncome - 2400001) * ((double) taxSlabs.get(">24L") / 100));
            }
            var cess = (int) (taxAmount * ((double) 4 / 100));
            taxAmount += cess;
            var pt = 2400;
            taxAmount += pt;
            var takeHome = Math.round(grossSalary / 12) - Math.round((double) taxAmount / 12) - employeePFPerMonth;
            var mealCardPerMonth = salaryStructure.getMealCard() / 12;
            var totalTakeHome = takeHome + mealCardPerMonth;

            if(salaryStructure == salaries.get(0)){
                currentTakeHome = totalTakeHome;
            }
            System.out.println(salaryStructure.getPercentageIncrement().toUpperCase() + " : Gross Salary: " + grossSalary + ": TakeHome: " + takeHome
                    + ": MealCard: " + mealCardPerMonth + ": Total TakeHome: " + totalTakeHome + ": Increment in TakeHome: " + (totalTakeHome-currentTakeHome));
        }
    }

}

/**
 * Basic
 * House Rent Allowance
 * Conveyance Allowance
 * Telephone and Broadband
 * Meal Card
 * Employer NPS
 * Employer PF CTC
 * Gratuity
 * Medical Insurance
 */
@Data
class SalaryStructure {
    private String percentageIncrement;
    private double basic;
    private double hra;
    private double allowance;
    private double telephone;
    private double mealCard;
    private double nps;
    private double pf;
    private double gratuity;
    private double medicalInsurance;
    private double totalCTC;

    public SalaryStructure(String percentageIncrement, double totalCTC, int npsPercentage) {
        this.percentageIncrement = percentageIncrement;
        this.totalCTC = Math.round(totalCTC);
        this.basic = Math.round(totalCTC * ((double) 50 / 100));
        this.hra = Math.round(this.basic * ((double) 50 / 100));
        this.mealCard = 52800;
        this.pf = 21600;//Standard 21600
        this.telephone = 18000; // Standard 18000
        this.medicalInsurance = 14900; // Standard 14900
        this.nps = Math.round(this.basic * ((double) npsPercentage / 100));
        this.gratuity = Math.round(this.basic * ((double) 4.81 / 100));
        this.allowance = Math.round(this.totalCTC - (this.basic + this.hra + this.telephone + this.mealCard + this.nps + this.pf + this.gratuity + this.medicalInsurance));
    }
}