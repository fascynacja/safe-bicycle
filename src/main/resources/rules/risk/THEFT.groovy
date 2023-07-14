
import org.pysz.safebicycle.rules.model.Bicycle

@groovy.transform.BaseScript(rules.base.DecoratedBaseScript)
import groovy.transform.BaseScript;
def BigDecimal premium(Bicycle bicycle) {
    var riskBasePremium = riskBasePremium("THEFT")
    var sumInsuredFactor =   sumInsuredFactor(sumInsured(bicycle))
    return riskBasePremium * sumInsuredFactor;
}


def BigDecimal sumInsured( Bicycle bicycle) {
    return bicycle.getSumInsured();
}