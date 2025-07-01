package org.puviyan.visitor.management.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitorRequest {

    private String name;
    private String phoneNumber;
    private String email;
    private boolean loggedOut;
    private String identityProof;
    private String identityNumber;
    private String puviyanBuddy;
}
