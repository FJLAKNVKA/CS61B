// TODO: Make sure to make this class a part of the synthesizer package
package synthesizer;

//Make sure this class is public
public class GuitarString {
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        buffer = new ArrayRingBuffer<>((int)Math.round(SR/frequency));
        for(int i=0;i<buffer.capacity();i++) buffer.enqueue(0.0);
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
       for(int i=0;i<buffer.capacity();i++)
       {
           buffer.dequeue();
           buffer.enqueue(Math.random()-0.5);
       }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        double t = buffer.dequeue();
        buffer.enqueue((t+buffer.peek())*0.5*DECAY);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }
}
