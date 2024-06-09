package com.springgcp.springgcp.controller;

        import com.springgcp.springgcp.message.ResponseMessage;
        import com.springgcp.springgcp.service.GcpService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.CrossOrigin;
        import org.springframework.web.bind.annotation.PostMapping;
        import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin("http://localhost:4200")
@Controller
@RequestMapping("/api/excel")
public class GcpFileHandleController {

    @Autowired
    GcpService gcpService;
    @PostMapping("/uploadFileToGCS")
    public ResponseEntity<ResponseMessage> uploadFileToGCS() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(gcpService.uploadFileToGCS()));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("Upload Failed"));
        }
    }

}
