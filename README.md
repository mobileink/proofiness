# proofiness

Proofing, testing, documentation for Clojure

## Colbert Logic

Instead of true and false, we have truthy and falsey.

Only one logical operator, "andy".  Defined by the following truthiness table:

| a | b | a andy b |
|---|---|----------|
| truthy | truthy | truthy |
| truthy | falsey | truthy |
| falsey | truthy | truthy |
| falsey | falsey | falsey |
