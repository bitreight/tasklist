package com.bitreight.tasklist.service.impl;

import org.springframework.stereotype.Service;

/**
 * Created by bitreight on 12.12.2016.
 */

@Service("securityService")
public class SecurityService {

    public boolean isOwner() {
        return false;
    }
}
