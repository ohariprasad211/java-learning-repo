/*

 //Logger Assertion Tests
@ParameterizedTest
@MethodSource("provideBaseMessageTestData")
void shouldLogAcknowledgedMessage_whenMessageExists(BaseMessage baseMessage, String expectedLog) {

	var logger = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(OutboundMessageProcessor.class);

	ListAppender<ch.qos.logback.classic.spi.ILoggingEvent> listAppender = new ch.qos.logback.core.read.ListAppender<>();
	listAppender.start();
	logger.addAppender(listAppender);

	outboundMessageProcessor.processAilOutPutStreamMessage(baseMessage);

	List<String> logs = listAppender.list.stream()
			.map(ILoggingEvent::getFormattedMessage)
			.toList();

	assertThat(logs).anyMatch(log -> log.contains(expectedLog));

}


//Mocking Static method
try (MockedStatic<CommonUtility> mockedStatic = org.mockito.Mockito.mockStatic(CommonUtility.class)) {
	mockedStatic.when(() -> CommonUtility.getXmlObject(any(String.class), any(Class.class)))
			.thenReturn(null);
	var inputStr = TestUtil.getContainerMoveEvent();
	assertThrows(IllegalStateException.class, () -> inboundMessageProcessor.processMessage(inputStr));
}

git worktree add ../project-main main
git worktree remove E:/Maersk/CodeBase/project-main


kafka ui timestamp filter
// Define start and end timestamps in milliseconds
def start = 1758503400000  // 22-Jul-2025 20:00:00 IST
def end = 1758514799000    // 22-Jul-2025 23:59:59 IST

timestampMs >= start && timestampMs <= end

*/
