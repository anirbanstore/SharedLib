package home.app.shared.model.vo;

import oracle.jbo.server.ViewDefImpl;
import oracle.jbo.server.ViewObjectImpl;

public class BaseViewObjectImpl extends ViewObjectImpl {
    
    public BaseViewObjectImpl(String string, ViewDefImpl viewDefImpl) {
        super(string, viewDefImpl);
    }

    public BaseViewObjectImpl() {
        super();
    }
}
