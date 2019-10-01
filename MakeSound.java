import java.io.*;
import javax.sound.midi.*;

public class MakeSound {
    Synthesizer s;
    MidiChannel c;
    public static void main(String[] args) {
        if (args.length<1) {
            System.out.println("Please enter a filename as an argument");
            return;
        }
        File f = new File(args[0]);
        if (!f.exists()) {
            System.out.println("Specified file does not exist");
            return;
        }
        if (!f.canRead()) {
            System.out.println("Read permission is not granted on specified file");
            return;
        }
        MakeSound ms = new MakeSound();
        try {
            InputStream in = new FileInputStream(f);
            long flen = f.length();
            if (flen > Integer.MAX_VALUE) {
                System.out.println("File is too large, max file size is: " + Integer.MAX_VALUE);
                return;
            }
            int flenint = (int)flen;
            for(int bi=0;bi<flenint-1024;bi++) {
                int aclen = 1024;
                if (bi+aclen > flenint) {
                    aclen = flenint-bi;
                }
                byte[] bread = new byte[aclen];
                try {
                    in.read(bread, bi, aclen);
                } catch (Exception e) {
                    System.out.println("Failed to read byte segment from file");
                    e.printStackTrace();
                    return;
                }
                ms.playSound(bread);
            }
        } catch (Exception e) {
            System.out.println("An error occured in reading the file");
            e.printStackTrace();
        }
    }
    public MakeSound() {
        try {
            s = MidiSystem.getSynthesizer();
            s.open();
            c = s.getChannels()[1];
        } catch (Exception e) {
            System.out.println("Failed to establish MIDI");
            e.printStackTrace();
            System.exit(-1);
        }
    }
    public void playSound(byte[] b) {
        for(byte by : b) {
            int num = (int)by;
            num = Math.abs(num);
            c.noteOn(num,50);
            try {
                Thread.sleep(20);
            } catch (Exception e){
                e.printStackTrace();
            }
            c.noteOff(num,50);
        }
    }
}