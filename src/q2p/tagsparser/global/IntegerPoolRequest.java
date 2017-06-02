package q2p.tagsparser.global;

import java.io.IOException;
import q2p.tagsparser.global.HardAcess.Flushable;

public abstract class IntegerPoolRequest {
	private Completition completition = Completition.ENDED;
	private HardAcess hard = null;
	
	protected final void flush(final Flushable flushable) {
		hard.flush(flushable);
	}
	
	private final String hardName;
	private final String requestMessage;
	
	protected IntegerPoolRequest(final String hardName, final String requestMessage) {
		this.hardName = hardName;
		this.requestMessage = requestMessage;
	}
	
	public final void collectInit(final boolean haveWork) {
		completition = Completition.haveWork(haveWork);
	}

	private final void finalizePool() {
		synchronized(completition) {
			completition = Completition.ENDED;
			hard = hard.kill();
		}
	}
	
	private final void init() {
		try {
			hard = new HardAcess(hardName);
		} catch (final IOException e) {
			Assist.abort(e.getMessage());
		}
		pool = new IntegerPool(1, 1);
		completition = Completition.IN_PROGRESS;
	}
	
	private IntegerPool pool = null;
	
	public final void collect() {
		synchronized(completition) {
			if(completition == Completition.ENDED)
				return;
			if(completition == Completition.READY)
				init();
		}
		while(true) {
			final int pick = pool.next();
			if(pick == -1)
				return;
			
			System.out.println(requestMessage+pick);
			
			try {
				receiveBody(pick);
			} catch (final Exception e) {
				e.printStackTrace();
				Assist.abort(e.getMessage()+"\nВозможно нужно обновить куки заголовок.");
				return;
			}
		}
	}
	
	protected final void answer(final int value, final boolean haveMore) {
		if(pool.answer(value, haveMore))
			finalizePool();
	}

	protected abstract void receiveBody(final int pick) throws IOException;
}