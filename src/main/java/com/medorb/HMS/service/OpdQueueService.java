//package com.medorb.HMS.service;
//
//import com.medorb.HMS.model.OpdQueue;
//import java.util.List;
//import java.util.Optional;
//
//public interface OpdQueueService {
//    List<OpdQueue> getAllOpdQueues();
//    Optional<OpdQueue> getOpdQueueById(Integer queueId);
//    OpdQueue createOpdQueue(OpdQueue opdQueue);
//    OpdQueue updateOpdQueue(Integer queueId, OpdQueue opdQueue);
//    void deleteOpdQueue(Integer queueId);
//    List<OpdQueue> getOpdQueuesByHospitalId(Integer hospitalId); // Get queue for a hospital
//    Optional<OpdQueue> getOpdQueueByPatientId(Integer patientId); // Get queue entry for a patient
//    OpdQueue enqueuePatientToQueue(OpdQueue opdQueue); // Method for enqueueing patient
//    OpdQueue dequeuePatientFromQueue(Integer queueId); // Method for dequeueing patient
//}

package com.medorb.HMS.service;

import com.medorb.HMS.model.OpdQueue;
import java.util.List;
import java.util.Optional;

public interface OpdQueueService {
    List<OpdQueue> getAllOpdQueueEntries();
    Optional<OpdQueue> getOpdQueueEntryById(Integer opdQueueId);
    OpdQueue createOpdQueueEntry(OpdQueue opdQueue);
    OpdQueue updateOpdQueueEntry(Integer opdQueueId, OpdQueue opdQueue);
    void deleteOpdQueueEntry(Integer opdQueueId);

    // Custom service methods (matching repository custom methods)
    List<OpdQueue> getOpdQueueEntriesByHospitalId(Integer hospitalId);
    List<OpdQueue> getOpdQueueEntriesByDoctorId(Integer doctorId); // If you keep Doctor relationship
    List<OpdQueue> getOpdQueueEntriesByPatientId(Integer patientId);
    List<OpdQueue> getOpdQueueEntriesByStatus(String queueStatus);
    List<OpdQueue> getCurrentOpdQueueForDoctor(Integer doctorId); // If you keep Doctor relationship
}