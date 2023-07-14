
import org.pysz.safebicycle.rules.model.Bicycle

@groovy.transform.BaseScript(rules.base.DecoratedBaseScript)
import groovy.transform.BaseScript

BigDecimal premium(Bicycle bicycle)   {
    var riskBasePremium = riskBasePremium("THIRD_PARTY_DAMAGE")
    var sumInsuredFactor = sumInsuredFactor(sumInsured(bicycle))
    var riskCountFactor =riskCountFactor(bicycle)
    return riskBasePremium * sumInsuredFactor * riskCountFactor
}


BigDecimal sumInsured(Bicycle bicycle) {
    return new BigDecimal("100.0")
}