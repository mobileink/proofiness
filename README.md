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

> What's the point?  Just that a conceptual shift from testing to proving is possible and has benefits, even though we cannot provide genuine formal proofs of the correctness of Clojure code.

## Colbert Logic

Instead of truth, truthiness; instead of true and false, truthy and falsey.

Only one logical operator, "andy".  Defined by the following truthiness table:

| a | b | a andy b |
|---|---|----------|
| truthy | truthy | truthy |
| truthy | falsey | truthy |
| falsey | truthy | truthy |
| falsey | falsey | falsey |
