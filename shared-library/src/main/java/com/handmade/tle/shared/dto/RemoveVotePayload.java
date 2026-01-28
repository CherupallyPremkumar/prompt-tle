package com.handmade.tle.shared.dto;

public class RemoveVotePayload {
    public String voterId;
    public String voteType; // "UP" or "DOWN"
    public long timestamp;
}
