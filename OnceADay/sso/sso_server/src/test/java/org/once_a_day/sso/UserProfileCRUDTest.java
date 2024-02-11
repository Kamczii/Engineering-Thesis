//package org.once_a_day.sso;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.producer.Producer;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.junit.jupiter.api.*;
//import org.once_a_day.database.enums.FileType;
//import org.once_a_day.database.model.FileDetails;
//import org.once_a_day.database.model.User;
//import org.once_a_day.exception.ResourceNotFoundException;
//import org.once_a_day.file_storage.file_storage_api.kafka.FileStorageKafkaTopics;
//import org.once_a_day.file_storage.file_storage_api.kafka.request.UploadProfilePictureRequestDTO;
//import org.once_a_day.sso.repository.FileRepository;
//import org.once_a_day.sso.dto.CreateProfileDTO;
//import org.once_a_day.sso.dto.ProfileDTO;
//import org.once_a_day.sso.dto.UpdateProfileDTO;
//import org.once_a_day.sso.dto.kafka.ProfileKafkaTopics;
//import org.once_a_day.sso.dto.kafka.response.ProfilePictureChangedDTO;
//import org.once_a_day.sso.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.*;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.listener.ContainerProperties;
//import org.springframework.kafka.listener.KafkaMessageListenerContainer;
//import org.springframework.kafka.listener.MessageListener;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//import org.springframework.kafka.test.EmbeddedKafkaBroker;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.kafka.test.core.BrokerAddress;
//import org.springframework.kafka.test.utils.ContainerTestUtils;
//import org.springframework.kafka.test.utils.KafkaTestUtils;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//import static org.assertj.core.api.Java6Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertInstanceOf;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@EntityScan({"org.once_a_day.database.model"})
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@EmbeddedKafka(topics = FileStorageKafkaTopics.UPLOAD_PROFILE_PICTURE_KAFKA_TOPIC)
//@AutoConfigureMockMvc
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestPropertySource(properties = {
//        "spring.kafka.bootstrap-servers = ${spring.embedded.kafka.brokers}",
//        "spring.kafka.trusted-packages = org.once_a_day.file_storage.file_storage_api.kafka.*, org.once_a_day.sso.dto.kafka.*"
//})
////@Import(TestConfig.class)
//public class UserProfileCRUDTest extends AbstractTestClass {
//
//    public static final String DESCRIPTION = "Im student";
//    public static final String NAME = "Kamil";
//
//    @LocalServerPort
//    private int port;
//
//    private String localhost = "http://localhost";
//
//    private String baseUrl = "";
//
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    private EmbeddedKafkaBroker kafkaEmbedded;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private BlockingQueue<ConsumerRecord<String, Object>> records;
//
//    private KafkaMessageListenerContainer<String, Object> container;
//    private Producer<String, Object> producer;
//    @Autowired
//    private FileRepository fileRepository;
//    @BeforeEach
//    void init() {
//        JsonDeserializer<Object> domainEventJsonDeserializer = new JsonDeserializer<>();
//        domainEventJsonDeserializer.addTrustedPackages("*");
//        DefaultKafkaConsumerFactory<String, Object> consumerFactory = new DefaultKafkaConsumerFactory<>(getConsumerProperties(), new StringDeserializer(), domainEventJsonDeserializer);
//        ContainerProperties containerProperties = new ContainerProperties(FileStorageKafkaTopics.UPLOAD_PROFILE_PICTURE_KAFKA_TOPIC);
//        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
//        records = new LinkedBlockingQueue<>();
//        container.setupMessageListener((MessageListener<String, Object>) records::add);
//        container.start();
//        System.out.println("TEST" + container.getGroupId() + Arrays.stream(kafkaEmbedded.getBrokerAddresses()).map(BrokerAddress::toString).collect(Collectors.joining("\n")) + " TOPIC" + String.join(",", kafkaEmbedded.getTopics()));
//        ContainerTestUtils.waitForAssignment(container, kafkaEmbedded.getPartitionsPerTopic());
//
//        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(kafkaEmbedded));
//        producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new JsonSerializer<>()).createProducer();
//    }
//
//    private Map<String, Object> getConsumerProperties() {
//        return KafkaTestUtils.consumerProps("file-storage-group", "false", kafkaEmbedded);
//    }
//    @AfterEach
//    void tearDown() {
//        container.stop();
//    }
//    @BeforeEach
//    public void setUp() {
//        baseUrl = localhost.concat(":").concat(port + "");
//    }
//
//    @Test
//    public void testProfileCru() throws Exception {
//        final long FIRST_USER_ID = 1L;
//
//        //CREATE
//        final ResponseEntity<Void> response1 = createProfileForUser(FIRST_USER_ID);
//        final ResponseEntity<Void> response2 = createProfileForUser(2L);
//        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(userRepository.count()).isEqualTo(2);
//
//        //READ
//        final var response = getProfileById(FIRST_USER_ID);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isNotNull();
//        assertThat(response.getBody().getDescription()).isEqualTo(DESCRIPTION);
//        assertThat(response.getBody().getName()).isEqualTo(NAME);
//
//        mvc.perform(get(baseUrl + "/users/" + 3L + "/profile")
//                        .header("Authorization", token())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
//
//        //UPDATE
//        final var NEW_DESCRIPTION = "NEW DESCRIPTION";
//        final var NEW_NAME = "NEW NAME";
//        final UpdateProfileDTO newProfile = UpdateProfileDTO.builder().name(NEW_NAME).description(NEW_DESCRIPTION).build();
//        updateUserProfile(FIRST_USER_ID, newProfile);
//        final var updated = getProfileById(FIRST_USER_ID);
//        assertThat(updated.getBody()).isNotNull();
//        assertThat(updated.getBody().getDescription()).isEqualTo(NEW_DESCRIPTION);
//        assertThat(updated.getBody().getName()).isEqualTo(NEW_NAME);
//    }
//
//    @Test
//    public void test_profile_picture_change_request() throws IOException, InterruptedException {
//
//        //given
//        Long userId = 1L;
//        createProfileForUser(userId);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        Resource stateFile = new ClassPathResource("test.png");
//        MultiValueMap<String, Object> body
//                = new LinkedMultiValueMap<>();
//        body.add("picture", stateFile);
//        HttpEntity<MultiValueMap<String, Object>> requestEntity
//                = new HttpEntity<>(body, headers);
//
//        //when
//        final ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl + "/users/" + userId + "/profile/picture", requestEntity, Void.class);
//
//        //then
//        assertThat(response.getBody()).isNull();
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//
//        ConsumerRecord<String, Object> message = records.poll(1, TimeUnit.SECONDS);
//        assertThat(message).isNotNull();
//        assertInstanceOf(UploadProfilePictureRequestDTO.class, message.value());
//    }
//
//    @Test
//    public void after_new_picture_upload() throws InterruptedException {
//        //given
//        final long FILE_ID = 1L;
//        final long USER_ID = 1L;
//        createProfileForUser(USER_ID);
//        fileRepository.save(FileDetails.builder()
//                .id(FILE_ID)
//                .type(FileType.PROFILE_IMAGE)
//                .fileName("test")
//                .bucket("test")
//                .build());
//        //when
//        producer.send(new ProducerRecord<>(ProfileKafkaTopics.PROFILE_PICTURE_UPLOADED_KAFKA_TOPIC, ProfilePictureChangedDTO.builder()
//                .userId(USER_ID)
//                .fileId(FILE_ID)
//                .build()));
//        producer.flush();
//
//        Thread.sleep(2000);
//        final User profile = userRepository.findById(USER_ID).orElseThrow(() -> new ResourceNotFoundException(User.class, USER_ID));
//        assertThat(profile.getAvatar().getId()).isEqualTo(fileRepository.findById(FILE_ID).get().getId());
//
//    }
//
//    private ResponseEntity<Void> createProfileForUser(Long userId) {
//        CreateProfileDTO profile = CreateProfileDTO.builder()
//                .description(DESCRIPTION)
//                .name(NAME)
//                .build();
//        return restTemplate.postForEntity(baseUrl + "/users/" + userId + "/profile", profile, Void.class);
//    }
//
//    private void updateUserProfile(Long userId, UpdateProfileDTO updateProfileDTO) {
//        restTemplate.put(baseUrl + "/users/" + userId + "/profile", updateProfileDTO, Void.class);
//    }
//
//    private ResponseEntity<ProfileDTO> getProfileById(Long userId) {
//        return restTemplate.getForEntity(baseUrl + "/users/" + userId + "/profile", ProfileDTO.class);
//    }
//
//
//
//}
