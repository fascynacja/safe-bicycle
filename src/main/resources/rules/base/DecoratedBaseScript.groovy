package rules.base

import org.pysz.safebicycle.rules.model.Bicycle

import java.time.LocalDate

import static rules.base.Constants.*

class DecoratedBaseScript extends BaseScript {

    def riskBasePremium(String riskType) {
        getRiskBasePremiumData()
                .find { map -> (map[RISK_TYPE_KEY] == riskType) }
                .get(PREMIUM_KEY)
    }


    BigDecimal sumInsuredFactor(BigDecimal riskSumInsured) {

        var sumInsuredFactors = sumInsuredFactors(riskSumInsured)
        if (sumInsuredFactors == null) {
            return BigDecimal.ZERO // TODO this is an assumption which needs to be claer out
        }
        def factorMin = sumInsuredFactors[FACTOR_MIN]
        def factorMax = sumInsuredFactors[FACTOR_MAX]
        def valueTo = sumInsuredFactors[VALUE_TO]
        def valueFrom = sumInsuredFactors[VALUE_FROM]
        var sumInsuredFactor = factorMax - (factorMax - factorMin) * (valueTo - riskSumInsured) / (valueTo - valueFrom)
        return sumInsuredFactor
    }

    // TODO what if suminsured does not find anything
    def sumInsuredFactors(double riskSumInsured) {
        getSumInsuredFactorData()
                .find { map -> (map[VALUE_FROM] <= riskSumInsured && map[VALUE_TO] >= riskSumInsured) }
    }

    def bicycleAgeFactor(Bicycle bicycle) {
        final ageActual = LocalDate.now().getYear() - bicycle.getManufactureYear()
        var bicycleAgeFactors = bicycleAgeFactors(ageActual, bicycle.getMake(), bicycle.getModel())
        def factorMin = bicycleAgeFactors[FACTOR_MIN]
        def factorMax = bicycleAgeFactors[FACTOR_MAX]
        def valueFrom = bicycleAgeFactors[VALUE_FROM]
        def valueTo = bicycleAgeFactors[VALUE_TO]
        var bicycleAgeFactor = factorMax - (factorMax - factorMin) * (valueTo - ageActual) / (valueTo - valueFrom)
        return bicycleAgeFactor
    }

    def bicycleAgeFactors(int ageActual, String make, String model) {
        bicycleAgeFactorData(ageActual, make, model)
    }

    def bicycleAgeFactorData(int ageActual, String make, String model) {
        def factorData = getAgeFactorData()
                .find { map ->
                    map['MAKE'] == make &&
                            map['MODEL'] == model &&
                            map['VALUE_FROM'] <= ageActual &&
                            map['VALUE_TO'] >= ageActual
                }
        if (factorData != null) {
            return factorData
        }
        factorData = getAgeFactorData()
                .find { map ->
                    map['MAKE'] == make &&
                            map['VALUE_FROM'] <= ageActual &&
                            map['VALUE_TO'] >= ageActual
                }
        if (factorData != null) {
            return factorData
        }
        factorData = getAgeFactorData()
                .find { map ->
                    map['VALUE_FROM'] <= ageActual &&
                            map['VALUE_TO'] >= ageActual
                }
        return factorData
    }


    double riskCountFactor(Bicycle bicycle) {
        //TODO validation of risks, do they exist? if not then what should we do?
        def risksCount = bicycle.getRisks().size()
        def riskCountData = getRiskCountFactorData()
                .find { map ->
                    map["VALUE_TO"] >= risksCount && map["VALUE_FROM"] <= risksCount
                }

        def factorMin = riskCountData[FACTOR_MIN]
        def factorMax = riskCountData[FACTOR_MAX]
        def valueTo = riskCountData[VALUE_TO]
        def valueFrom = riskCountData[VALUE_FROM]
        var factor = factorMax - (factorMax - factorMin) * (valueTo - risksCount) / (valueTo - valueFrom)
        return factor
    }

}
