package dip.fsd;

import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import java.util.List;
import java.util.Scanner;

public class packetcapture {

    public static void capture() throws Exception {

        List<PcapNetworkInterface> devices =
                Pcaps.findAllDevs();

        if (devices.isEmpty()) {
            System.out.println("No network interfaces found");
            return;
        }

        // Display network interfaces
        for (int i = 0; i < devices.size(); i++) {
            System.out.println(i + " : "
                    + devices.get(i).getName()
                    + " - "
                    + devices.get(i).getDescription());
        }

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Wi-Fi interface number: ");
        int index = scanner.nextInt();

        if (index < 0 || index >= devices.size()) {
            System.out.println("Invalid interface number");
            return;
        }

        PcapNetworkInterface device = devices.get(index);

        try (PcapHandle handle = device.openLive(
                65536,
                PcapNetworkInterface.PromiscuousMode
                        .NONPROMISCUOUS,
                1000)) {

            System.out.println("\nCapturing live packets...\n");

           while (true) {
    Packet packet = handle.getNextPacket();
    if (packet == null) continue;

    IpV4Packet ip4 = packet.get(IpV4Packet.class);
    IpV6Packet ip6 = packet.get(IpV6Packet.class);
    if (ip4 == null && ip6 == null) continue;

    String src, dst, proto;
    if (ip4 != null) {
        src = ip4.getHeader().getSrcAddr().getHostAddress();
        dst = ip4.getHeader().getDstAddr().getHostAddress();
        proto = ip4.getHeader().getProtocol().toString();
    } else {
        src = ip6.getHeader().getSrcAddr().getHostAddress();
        dst = ip6.getHeader().getDstAddr().getHostAddress();
        proto = ip6.getHeader().getProtocol().toString();
    }

    System.out.println("---------------------");
    System.out.println("Source IP: " + src);
    System.out.println("Destination IP: " + dst);
    System.out.println("Protocol: " + proto);
    System.out.println("Packet Size: " + packet.length() + " bytes");

    TcpPacket tcp = packet.get(TcpPacket.class);
    UdpPacket udp = packet.get(UdpPacket.class);

    if (tcp != null) {
        System.out.println("Source Port: " + tcp.getHeader().getSrcPort().valueAsInt());
        System.out.println("Destination Port: " + tcp.getHeader().getDstPort().valueAsInt());
    } else if (udp != null) {
        System.out.println("Source Port: " + udp.getHeader().getSrcPort().valueAsInt());
        System.out.println("Destination Port: " + udp.getHeader().getDstPort().valueAsInt());
    }
}
                }
                

                }
        }