package home.app.shared.model.eo;

import oracle.jbo.server.EntityImpl;
import oracle.jbo.server.SequenceImpl;
import oracle.jbo.domain.Number;

public class BaseEntityImpl extends EntityImpl {
    
    public BaseEntityImpl() {
        super();
    }

    public Number sequenceNextval(final String sequenceName) {
        SequenceImpl sequence = new SequenceImpl(sequenceName, getDBTransaction());
        return sequence.getSequenceNumber();
    }
}
