package com.example.iptimeAPI.util.iptime.service;

import java.io.IOException;
import java.util.List;

public interface IptimeService {

    List<String> getLatestMacAddressesList() throws IOException;

}
