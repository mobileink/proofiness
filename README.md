# proofiness

Proofing, testing, documentation for Clojure

## Testing and Proof

Suppose we have a binary function `add2`, which adds two numbers.  To test this using a Clojure unit test framework, we would write something like `(= (add2 1 1) 2)` within the framework.
  
From a testing perspective, this is, well, a test.  When you run it, it "tests" the implementation, yielding either success or failure.

But from a logical perspective, this amounts to a _theorem_ about `add2`.  More precisely, language fragments like `(= (add2 1 1) 2)` are formulae (sentences) that denote or express theorems (propositions).  Theorems/propositions are abstract mathematical objects; formulae/sentences are concrete (empirical) syntactic objects.  Usually it is not necessary to keep track of this distinction, so we will use "theorem" to refer to sentences and resort to more careful language only where ambiguity lurks.

A collection of such theorems ('test cases') amounts to a _theory_ of `add2`.

Formally, a theorem is a statement or proposition that is ultimately reducible to a combination of axioms.  Very roughly, to prove a theorem, you show that it can be constructed from nothing more than the axioms together with rules of inference.

A test that runs successfully corresponds to a proof of the theorem that is expressed by the text of the test. 

> Actually, `(= (add2 1 1) 2)` is a meta-theorem rather than a theorem, since `=` is not part of the definition of `add2`, `1`, or `2`.  And since `=` is defined in Clojure, a more accurate way to express the theorem would be something like `(--> (add2 1 1) 2)`, where `-->` is a meta-variable not defined in the target language (Clojure).  This expresses the idea that `(add2 1 1)` _reduces_ to `2`; or, alternatively, that the _normal forms_ of `(add2 1 1)` and `2` are equivalent.  Which means that `(add2 1 1)` can always be replaced by `2` in the program text (and vice-versa), which is not quite the same thing as saying that the two expressions are equal, or denote equal things.  If we were to write `(= (add2 1 1) 2)`, this would be a theorem not only about `add2` (and `1` and `2`), but also about `=`.  To prove it using a test, we would have to write `(--> (= (add2 1 1) 2) true)`.

In any case, given a function like `add2`, we can in principle derive an infinite number of theorems, one for each pair of arguments.  Each such theorem could be proven by running the test ("executing the theorem?").  But such tests provide only partial proof of `add2`; to genuinely prove that `add2` is correct, we would have to run through every possible input.  This is impossible in principle, since the domain of `add2` is infinite.

But a partial proof is better than no proof.

> What's the point?  Just that a conceptual shift from testing to proving is possible and has benefits, even though we cannot provide complete formal proofs of the correctness of Clojure code.  Tests as theorems nonetheless should be counted as genuine partial proofs.  Not formal proofs (since for that we would have to prove the correctness of the Clojure compiler and runtime, among other things), but pragmatic proofs: in practice, we assume that a program that passes its unit tests is correct, at least for those bits that are tested.  So we should treat tests as (potential) proofs.  "Potential" because they only become actual proofs when executed.  (Note that this only applies to functional languages without side-effects.  Tests of imperative language procedures with possible side effects can never be construed as even partial proof, since in such languages there is no guarantee that the same input will always produce the same output.

> Or maybe this is the point: we can learn a thing or two about programs and proofs from what researchers and designers working on proof assistants, automated theorem provers, and the like have learned.  Clojure is not a proof system, there's no point in even trying to formally prove anything about Clojure programs; but nonetheless it is functional, side-effect free by default, has immutable data, etc. all of which means that good informal reasoning about Clojure programs is, well, reasonable.  Even if we cannot use automated proof procedures and so forth, we can steal the expressive bits from these languages.  And the same goes for non-constructive proofy stuff like Z and other ZFC-based logic systems.  We should be pillaging those villages for their valuables.

### Proof and Proofiness

The general idea here is that programmers should move in the general direction of proof (or proof theory, or something in that general vicinity).  So the obvious question is: what is a proof?  And the answer is: well, that's a little complicated.  Over the course of the 20th century the concept of mathematical/logical proof underwent major transformations; only recently has the notion begun to converge in a practical way with the concept of programming.  It must be admitted that the details are a little hairy, but on the other hand, so much has been written that it should be possible to present the bits of theory we need in a pleasing and digestible way that will not frighten off the average programmer.  In the end the concepts are simple, amazingly so; but they do require a major conceptual reorientation, which is not the easiest thing in the world to pull off.

## Colbert Logic

Instead of truth, truthiness; instead of true and false, truthy and falsey.

Only one logical operator, "andy".  Defined by the following truthiness table:

| a | b | a andy b |
|---|---|----------|
| truthy | truthy | truthy |
| truthy | falsey | truthy |
| falsey | truthy | truthy |
| falsey | falsey | falsey |
