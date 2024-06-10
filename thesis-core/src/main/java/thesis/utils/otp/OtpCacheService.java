package thesis.utils.otp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class OtpCacheService {
    @Value("${opt.expire-duration}")
    private int expireDuration;
    @Value("${opt.resend-duration}")
    private int resendDuration;
    private final ConcurrentHashMap<String, String> otpCache = new ConcurrentHashMap<>();
    private final Map<String, Long> lastSentTime = new ConcurrentHashMap<>();
    private final ScheduledExecutorService somethingScheduler = Executors.newScheduledThreadPool(1);

    public void storeOtp(String key, String otp) {
        otpCache.put(key, otp);
        lastSentTime.put(key, Instant.now().getEpochSecond());
        somethingScheduler.schedule(() -> {
            otpCache.remove(key);
        }, expireDuration, TimeUnit.SECONDS);  // Xóa OTP sau 2 phút
    }

    public long canResend(String key) {
        Long lastSent = lastSentTime.get(key);
        if (lastSent == null) {
            return 0; // Ngay lập tức có thể gửi nếu chưa từng gửi
        }
        long secondsSinceLastSent = Instant.now().getEpochSecond() - lastSent;
        return secondsSinceLastSent < resendDuration ? resendDuration - secondsSinceLastSent : 0;
    }

    public boolean validateOtp(String key, String otp) {
        String storedOtp = otpCache.get(key);
        return storedOtp != null && storedOtp.equals(otp);
    }

    public void clearOtp(String key) {
        otpCache.remove(key);
        lastSentTime.remove(key);
    }

    public String generateOTP(int length) {
        String numbers = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(numbers.length());
            otp.append(numbers.charAt(index));
        }
        return otp.toString();
    }
}
