import groovy.transform.BaseScript
import org.pysz.safebicycle.rules.model.Bicycle

@groovy.transform.BaseScript(rules.base.DecoratedBaseScript)
import groovy.transform.BaseScript;

def BigDecimal premium(Bicycle bicycle)   {
    var riskBasePremium = riskBasePremium("THIRD_PARTY_DAMAGE")
    var sumInsuredFactor = sumInsuredFactor(sumInsured(bicycle))
    var riskCountFactor =riskCountFactor(bicycle)
    return riskBasePremium * sumInsuredFactor * riskCountFactor;
}


def BigDecimal sumInsured(Bicycle bicycle) {
    return new BigDecimal("100.0");
}