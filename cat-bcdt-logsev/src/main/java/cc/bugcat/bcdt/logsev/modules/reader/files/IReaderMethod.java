package cc.bugcat.bcdt.logsev.modules.reader.files;

import cc.bugcat.bcdt.logsev.modules.reader.ReaderFileSession;


/**
 *
 * @author bugcat-cc
 * */
@FunctionalInterface
public interface IReaderMethod<REQ> {
    
    
    Object execute(ReaderFileSession fileSession, REQ req) throws Exception;
    
    
}

