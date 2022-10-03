public class Palindrome {
    public Deque<Character> wordToDeque(String word)
    {
        Deque<Character> t= new ArrayDeque<>();
        for(int i = 0;i < word.length();i++)
        {
            t.addLast(word.charAt((i)));
        }
        return t;
    }

    public boolean isPalindrome(String word)
    {
        Deque<Character> t = wordToDeque(word);
        while(t.size()>1)
        {
            if(t.removeFirst()!=t.removeLast())return false;
        }
        if(!t.isEmpty())t.removeLast();
        return true;
    }

    public boolean isPalindrome(String word,CharacterComparator com)
    {
        Deque<Character> t = wordToDeque(word);
        while(t.size() > 1)
        {
            if(!com.equalChars(t.removeFirst(), t.removeLast()))return false;
        }
        if(!t.isEmpty())t.removeLast();
        return true;
    }
}
