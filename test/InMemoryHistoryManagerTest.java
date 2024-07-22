import managers.InMemoryHistoryManager;

class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {

    public InMemoryHistoryManagerTest() {
        super(new InMemoryHistoryManager());
    }
}