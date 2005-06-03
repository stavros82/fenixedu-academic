package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.Iterator;

import net.sourceforge.fenixedu.domain.masterDegree.GuideRequester;
import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import net.sourceforge.fenixedu.util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class Guide extends Guide_Base {

    public Guide() {
    }

    /**
     * @param guideId
     */
    public Guide(Integer guideId) {
        setIdInternal(guideId);
    }

    public Guide(Integer number, Integer year, Double total, String remarks, IPerson person,
            IContributor contributor, GuideRequester guideRequester, IExecutionDegree executionDegree,
            PaymentType paymentType, Date creationDate, Integer version) {
        this.setContributor(contributor);
        this.setNumber(number);
        this.setPerson(person);
        this.setRemarks(remarks);
        this.setTotal(total);
        this.setYear(year);
        this.setGuideRequester(guideRequester);
        this.setExecutionDegree(executionDegree);
        this.setPaymentType(paymentType);
        this.setCreationDate(creationDate);
        this.setVersion(version);
    }

    public String toString() {
        String result = "[GUIDE";
        result += ", codInt=" + getIdInternal();
        result += ", number=" + getNumber();
        result += ", year=" + getYear();
        result += ", contributor=" + getContributor();
        result += ", total=" + getTotal();
        result += ", remarks=" + getRemarks();
        result += ", guide Requester=" + getGuideRequester();
        result += ", execution Degree=" + getExecutionDegree();
        result += ", payment Type=" + getPaymentType();
        result += ", creation Date=" + getCreationDate();
        result += ", version=" + getVersion();
        result += ", payment Date=" + getPaymentDate();
        result += "]";
        return result;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IGuide) {
            IGuide guide = (IGuide) obj;

            if (((getNumber() == null && guide.getNumber() == null) || (getNumber().equals(guide
                    .getNumber())))
                    && ((getYear() == null && guide.getYear() == null) || (getYear().equals(guide
                            .getYear())))) {
                resultado = true;
            }
        }

        return resultado;
    }

    public IGuideSituation getActiveSituation() {
        if (this.getGuideSituations() != null) {
            Iterator iterator = this.getGuideSituations().iterator();
            while (iterator.hasNext()) {
                IGuideSituation guideSituation = (IGuideSituation) iterator.next();
                if (guideSituation.getState().equals(new State(State.ACTIVE))) {
                    return guideSituation;
                }
            }
        }
        return null;
    }

}
