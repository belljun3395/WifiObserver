package com.example.iptimeAPI.domain.iptime;

import java.io.IOException;
import java.util.List;

public interface IptimeService {

    List<String> getLatestMacAddressesList() throws IOException;

}
