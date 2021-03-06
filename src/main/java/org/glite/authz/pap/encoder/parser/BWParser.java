/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* Generated By:JavaCC: Do not edit this line. BWParser.java */
package org.glite.authz.pap.encoder.parser;
import java.lang.String;
import org.glite.authz.pap.common.xacml.wizard.*;
import org.opensaml.xacml.XACMLObject;
import org.opensaml.xacml.policy.EffectType;
import java.util.List;
import java.util.LinkedList;
import org.glite.authz.pap.common.xacml.utils.PolicyHelper;
import org.glite.authz.pap.common.xacml.utils.PolicySetHelper;
import org.opensaml.xacml.policy.PolicyType;

/**
 * Used to collect data from the input file and hold it for further rules
 * At any moment, at most one of this element is supposed to be filled.
 *
 * @author Vincenzo Ciaschini
 */
class MixIn {
    public RuleWizard rule;             // Keep track of a policy rule
    public ObligationWizard obligation; // Keep track of a policy obligation
    public String description;          // Keep track of a policy description
    public PolicyWizard policy;         // Keep track of a whole policy (for inclusion in policy sets)

    /**
     * Keep track of the RuleWizard object
     * @param RuleWizard the object to keep track of.
     */
    public MixIn(RuleWizard r) {
        rule = r;
    }

    /**
     * Keep track of the ObligationWizard object
     * @param ObligationWizard the object to keep track of.
     */
    public MixIn(ObligationWizard o) {
        obligation = o;
    }

    /**
     * Keep track of the description
     * @param String the description to keep track of.
     */
    public MixIn(String s) {
        description = s;
    }

    /**
     * Keep track of the PolicyWizard object
     * @param PolicyWizard the object to keep track of.
     */
    public MixIn(PolicyWizard pw) {
        policy = pw;
    }
};

public class BWParser implements BWParserConstants {

  static final public List<XACMLWizard> Text() throws ParseException {
  XACMLWizard psw = null;
  List<XACMLWizard> sets = new LinkedList<XACMLWizard>();
    label_1:
    while (true) {
      psw = Resource();
                    sets.add(psw);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ACTION:
      case RESOURCE:
      case PRIVATE:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
    }
    {if (true) return sets;}
    throw new Error("Missing return statement in function");
  }

  static final public XACMLWizard Resource() throws ParseException {
  List<MixIn> policyContents = new LinkedList<MixIn>();
  MixIn content=null;
  String t = null;
  String t3 = null;
  String t4 = null;
  List<AttributeWizard> obligationContent = new LinkedList<AttributeWizard>();
  List<MixIn> mixinContent = new LinkedList<MixIn>();
  AttributeWizard attr = null;
  MixIn mixin;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case RESOURCE:
      jj_consume_token(RESOURCE);
      t = TextString();
      jj_consume_token(22);
      label_2:
      while (true) {
        content = ResourceContents();
                                                               policyContents.add(content);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case DESCRIPTION:
        case OBLIGATION:
        case ACTION:
        case PRIVATE:
          ;
          break;
        default:
          jj_la1[1] = jj_gen;
          break label_2;
        }
      }
      jj_consume_token(23);
    PolicySetWizard psw = new PolicySetWizard(new AttributeWizard("resource", t));
    for (MixIn mixedin : policyContents) {
      if (mixedin.policy != null) {
        psw.addPolicy(mixedin.policy);
      }
      if (mixedin.obligation != null)
        psw.addObligation(mixedin.obligation);
      if (mixedin.description != null)
        psw.setDescription(mixedin.description);
    }
    {if (true) return psw;}
      break;
    case ACTION:
      jj_consume_token(ACTION);
      t3 = TextString();
      jj_consume_token(22);
      label_3:
      while (true) {
        mixin = ActionContent();
                                                         mixinContent.add(mixin);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case DESCRIPTION:
        case OBLIGATION:
        case RULE:
          ;
          break;
        default:
          jj_la1[2] = jj_gen;
          break label_3;
        }
      }
      jj_consume_token(23);
      PolicyWizard pw = new PolicyWizard(new AttributeWizard("action", t3));
      for (MixIn mixedin : mixinContent) {
        if (mixedin.rule != null)
          pw.addRule(mixedin.rule);
        if (mixedin.obligation != null)
          pw.addObligation(mixedin.obligation);
        if (mixedin.description != null)
          pw.setDescription(mixedin.description);
      }
      {if (true) return pw;}
      break;
    case PRIVATE:
      jj_consume_token(PRIVATE);
      jj_consume_token(ACTION);
      t4 = TextString();
      jj_consume_token(22);
      label_4:
      while (true) {
        mixin = ActionContent();
                                                                    mixinContent.add(mixin);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case DESCRIPTION:
        case OBLIGATION:
        case RULE:
          ;
          break;
        default:
          jj_la1[3] = jj_gen;
          break label_4;
        }
      }
      jj_consume_token(23);
      PolicyWizard pw2 = new PolicyWizard(new AttributeWizard("action", t4));
      for (MixIn mixedin : mixinContent) {
        if (mixedin.rule != null)
          pw2.addRule(mixedin.rule);
        if (mixedin.obligation != null)
          pw2.addObligation(mixedin.obligation);
        if (mixedin.description != null)
          pw2.setDescription(mixedin.description);
      }
      pw2.setPrivate(true);
      {if (true) return pw2;}
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public MixIn ResourceContents() throws ParseException {
  String t1 = null;
  String t2 = null;
  String t3 = null;
  String t4 = null;
  List<AttributeWizard> obligationContent = new LinkedList<AttributeWizard>();
  List<MixIn> mixinContent = new LinkedList<MixIn>();
  AttributeWizard attr = null;
  MixIn mixin;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case DESCRIPTION:
      jj_consume_token(DESCRIPTION);
      t1 = TextString();
      {if (true) return new MixIn(t1);}
      break;
    case OBLIGATION:
      jj_consume_token(OBLIGATION);
      t2 = TextString();
      jj_consume_token(22);
      label_5:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case STRING:
        case ID:
        case ID2:
          ;
          break;
        default:
          jj_la1[5] = jj_gen;
          break label_5;
        }
        attr = ObligationContent();
                                                                obligationContent.add(attr);
      }
      jj_consume_token(23);
      if (obligationContent.isEmpty())
        {if (true) return new MixIn(new ObligationWizard(t2));}
      else
        {if (true) return new MixIn(new ObligationWizard(t2, obligationContent));}
      break;
    case ACTION:
      jj_consume_token(ACTION);
      t3 = TextString();
      jj_consume_token(22);
      label_6:
      while (true) {
        mixin = ActionContent();
                                                         mixinContent.add(mixin);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case DESCRIPTION:
        case OBLIGATION:
        case RULE:
          ;
          break;
        default:
          jj_la1[6] = jj_gen;
          break label_6;
        }
      }
      jj_consume_token(23);
      PolicyWizard pw = new PolicyWizard(new AttributeWizard("action", t3));
      for (MixIn mixedin : mixinContent) {
        if (mixedin.rule != null)
          pw.addRule(mixedin.rule);
        if (mixedin.obligation != null)
          pw.addObligation(mixedin.obligation);
        if (mixedin.description != null)
          pw.setDescription(mixedin.description);
      }
      {if (true) return new MixIn(pw);}
      break;
    case PRIVATE:
      jj_consume_token(PRIVATE);
      jj_consume_token(ACTION);
      t4 = TextString();
      jj_consume_token(22);
      label_7:
      while (true) {
        mixin = ActionContent();
                                                                    mixinContent.add(mixin);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case DESCRIPTION:
        case OBLIGATION:
        case RULE:
          ;
          break;
        default:
          jj_la1[7] = jj_gen;
          break label_7;
        }
      }
      jj_consume_token(23);
      PolicyWizard pw2 = new PolicyWizard(new AttributeWizard("action", t4));
      for (MixIn mixedin : mixinContent) {
        if (mixedin.rule != null)
          pw2.addRule(mixedin.rule);
        if (mixedin.obligation != null)
          pw2.addObligation(mixedin.obligation);
        if (mixedin.description != null)
          pw2.setDescription(mixedin.description);
      }
      pw2.setPrivate(true);
      {if (true) return new MixIn(pw2);}
      break;
    default:
      jj_la1[8] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public AttributeWizard ObligationContent() throws ParseException {
  String t1 = null, t2=null;
    t1 = TextString();
    jj_consume_token(24);
    t2 = TextString();
    {if (true) return new AttributeWizard(t1, t2);}
    throw new Error("Missing return statement in function");
  }

  static final public MixIn ActionContent() throws ParseException {
  String t1 = null;
  String t2 = null;
  String t3 = null;
  String description = null;
  List<AttributeWizard> ruleContent = new LinkedList<AttributeWizard>();
  List<AttributeWizard> obligationContent = new LinkedList<AttributeWizard>();
  EffectType effect = null;
  AttributeWizard attr = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case DESCRIPTION:
      jj_consume_token(DESCRIPTION);
      t1 = TextString();
    {if (true) return new MixIn(t1);}
      break;
    case OBLIGATION:
      jj_consume_token(OBLIGATION);
      t2 = TextString();
      jj_consume_token(22);
      label_8:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case STRING:
        case ID:
        case ID2:
          ;
          break;
        default:
          jj_la1[9] = jj_gen;
          break label_8;
        }
        attr = ObligationContent();
                                                                obligationContent.add(attr);
      }
      jj_consume_token(23);
      if (obligationContent.isEmpty())
        {if (true) return new MixIn(new ObligationWizard(t2));}
      else
        {if (true) return new MixIn(new ObligationWizard(t2, obligationContent));}
      break;
    case RULE:
      jj_consume_token(RULE);
      effect = RuleEffect();
      jj_consume_token(22);
      label_9:
      while (true) {
        attr = RuleContent();
                           ruleContent.add(attr);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case STRING:
        case ID:
        case ID2:
          ;
          break;
        default:
          jj_la1[10] = jj_gen;
          break label_9;
        }
      }
      jj_consume_token(23);
    {if (true) return new MixIn(new RuleWizard(ruleContent, effect));}
      break;
    default:
      jj_la1[11] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public EffectType RuleEffect() throws ParseException {
 boolean result;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case DENY:
      jj_consume_token(DENY);
             {if (true) return EffectType.Deny;}
      break;
    case PERMIT:
      jj_consume_token(PERMIT);
             {if (true) return EffectType.Permit;}
      break;
    default:
      jj_la1[12] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public AttributeWizard RuleContent() throws ParseException {
  String t1=null, t2=null;
    t1 = TextString();
    jj_consume_token(24);
    t2 = TextString();
    {if (true) return new AttributeWizard(t1, t2);}
    throw new Error("Missing return statement in function");
  }

  static final public String TextString() throws ParseException {
  Token t = null;
  String s = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STRING:
      t = jj_consume_token(STRING);
    s = t.toString();
    {if (true) return s.substring(1, s.length()-1);}
      break;
    case ID:
      t = jj_consume_token(ID);
    s = t.toString();
    {if (true) return s.substring(1, s.length()-1);}
      break;
    case ID2:
      t = jj_consume_token(ID2);
    {if (true) return t.toString();}
      break;
    default:
      jj_la1[13] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static private boolean jj_initialized_once = false;
  static public BWParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  static public Token token, jj_nt;
  static private int jj_ntk;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[14];
  static private int[] jj_la1_0;
  static {
      jj_la1_0();
   }
   private static void jj_la1_0() {
      jj_la1_0 = new int[] {0x1a000,0x13800,0x5800,0x5800,0x1a000,0xe0000,0x5800,0x5800,0x13800,0xe0000,0xe0000,0x5800,0x600,0xe0000,};
   }

  public BWParser(java.io.InputStream stream) {
     this(stream, null);
  }
  public BWParser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  You must");
      System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new BWParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
  }

  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
  }

  public BWParser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  You must");
      System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new BWParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
  }

  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
  }

  public BWParser(BWParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  You must");
      System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
  }

  public void ReInit(BWParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
  }

  static final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.Vector jj_expentries = new java.util.Vector();
  static private int[] jj_expentry;
  static private int jj_kind = -1;

  static public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[25];
    for (int i = 0; i < 25; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 14; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 25; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  static final public void enable_tracing() {
  }

  static final public void disable_tracing() {
  }

//     public static String main(String args[]) throws ParseException {
//         BWParser parser = new BWParser(System.in);
//         try {
//             return parser.Text();  
//         }
//         catch (ParseException e) {
//             System.out.println("EXCEPTION");
//             System.out.println(e);
//         }
//     }
}
