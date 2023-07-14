import org.pysz.safebicycle.rules.model.Bicycle

@groovy.transform.BaseScript(rules.base.DecoratedBaseScript)
import groovy.transform.BaseScript;
def BigDecimal premium(Bicycle bicycle)   {
    var riskBasePremium = riskBasePremium("DAMAGE")
    var sumInsuredFactor = sumInsuredFactor(sumInsured(bicycle))
    var bicycleAgeFactor = bicycleAgeFactor(bicycle)
    return riskBasePremium * sumInsuredFactor * bicycleAgeFactor;
}


def BigDecimal sumInsured( Bicycle bicycle) {
    return bicycle.getSumInsured()/2.0;
}