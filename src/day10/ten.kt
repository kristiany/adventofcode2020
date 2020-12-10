package day10

import forLinesIn
import kotlin.math.pow

fun main() {
    forLinesIn("day10/input.txt") {
        var devices = it.map { it.toInt() }.sorted().toMutableList()
        devices.add(devices.last() + 3)

        val diffs = devices.mapIndexed { i, d ->
            d - (if (i > 0) devices[i - 1] else 0)
        }

        println("Part 1: ${diffs.filter { it == 1 }.count() * diffs.filter { it == 3 }.count()}")

        // Was on the right track, but didn't knew the math so stole some help from:
        // https://topaz.github.io/paste/#XQAAAQDXCQAAAAAAAAA4HUhobBw1MA7Jk3hz/X+83Nj7FORM5TPDqC1jez4fWkZyeEowYBZtF1q5Y2ETPy1EtzONcTW3+1dv6MNQ7QTNSNkJUqgweCDaPsGz+EIo9y94zgtvGHwy6XfZBNsFAls1Ji6i5o8bFywhkaolAwnou00xIuVAOY0R8Vcmjfx8KDnesnUW5TC9Vv64GOWyLF2igiA83uemR3jHuZdK5B0QrVOAKWg3Xs5x65KXQ7RWzAAhvnhCL7lMER14WnpPByFBYp0y8FIEyGS+oDxm8z18G5KZApFJ3mDfSD//dw12nlcPd48YDfVKNaa5QsTgtrFU7tR0tHAB/WvDxrDkQ9arAK2Noel0gs9ddqGPHeJnzIfwTJMnoLQ1wPkbeHjFEL8ucJgjTn82+Ws8TGeHW636ly72LfjPkptAwhUhXGuotGlk43GZziGd+g4GPx84M17BOd+65yIKvM70AwKAIU/vVA03LqUFR02z+K48RosEX/DuneTFBEMjCWCqJTh0761TUUOvCEP7EbQFfDLDWxImpd/8WQXjhfIjZ1G/VrLzBICubO+RLhbre+4Djrqdmq7rhEZuMhdSi53HTMZMnuX52xXP2vK3NlQDbih4iGs+9u6RoBv1GC3i85albi1Q2mK4i4XOf8hsEHiOmv1xWImWjlePXZfOD4kVX+hDmZjw0I0peUF3Pao+3WLRvYCR341Hqmn1t/u7DOFpTTYnJaKVbm+lwPkWpHsC5O4kVYZYUe7F5zspHuNfiBaJeEClBSShx73wL8HfPQ+fnvdXFkWyu7v8mLgg1P8Fm99xaFnly5ihZt3LtTuM8anbmyQTqasrcISzrvKFGYpCDzpcH7ISpaLUpYOAOgJ8NiZrNP3Hv93tLAlJHx+e/SlDWUU9WbnTXHhgeHabVWwkHwVPOTPVqCozRRyOjE8GdhBVxWwEuDFSBwmXslSh27yAUWSClI5aA4WbLp1SOKZeb/84mXeLGNwa9E0Bw/op6KrISvV2T3de6Rzxfxx9m/oN318m4gwvbI38NlQi9CtOYjWlM1KJIK16pqlmUc2anIjnef9DOXjnPILHevs8dtgdoJy4XMHMdg3HvpmgqBdS5r3TvdPbYjq7YHgSFQVTyIyYxApalZGQ2pMlFyEcMMcOzZ4MOy0ePzEonQtpZIl4m6419V6Mfl8EyeaccFcoi3tl3XEr09iA8KWMGb3jaqxusjKoEdUcJXY9okDdOJB1h0hlcbmlo3OZQOISGUpzLeGFOD1Bngg1bmQdSThAiSnXTA0dk7K7QyKAnLkY3knz5NJJ2mcUAm0V1LrmOIt6Unfvw4iv6bmqk4kyBqwWZ0Wf5GmKgrwwPy+HvfiicdLZMUyqWWIPyaEY63V2FiuvrQ3azewL3aPXGyF3qEXWGDHKCzP8ozMk/4hXkcqMuGbqD26NTvuAFXD1zbDIBT45trwIEYcaIvlIBPt4q8h75sHZfb9VacVXId38pnKAM/Md5Rl+S93Ah0mEELPwckj++muDtw==
        var count = intArrayOf(0, 0, 0, 0, 0, 0)
        var length = 1;
        var last = 0;
        for (d in devices) {
            val diff = d - last
            if (diff == 1) {
                ++length
            }
            if (d > last + 1) {
                ++count[length]
                length = 1
            }
            last = d
        }

        println("Part 2: ${(2.0.pow(count[3]) * 4.0.pow(count[4]) * 7.0.pow(count[5])).toLong()}")

    }
}
