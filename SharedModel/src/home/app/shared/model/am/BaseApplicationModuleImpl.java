package home.app.shared.model.am;

import oracle.jbo.domain.Number;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.SequenceImpl;

public class BaseApplicationModuleImpl extends ApplicationModuleImpl {
    
    public BaseApplicationModuleImpl() {
        super();
    }

    public Number sequenceNextval(final String sequenceName) {
        SequenceImpl sequence = new SequenceImpl(sequenceName, getDBTransaction());
        return sequence.getSequenceNumber();
    }
}
