package com.koral.sockservice.util;

import com.koral.sockservice.model.Socks;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

public interface SocksParser {
    public ArrayList<Socks> parseSocks(MultipartFile multipartFile);
}
