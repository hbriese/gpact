/*
 * Copyright ConsenSys Software Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package net.consensys.gpact.trie;

import static org.apache.logging.log4j.LogManager.getLogger;
import static org.hyperledger.besu.crypto.Hash.keccak256;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import org.apache.logging.log4j.Logger;
import org.apache.tuweni.bytes.Bytes;

class BinaryNodeFactory<V> implements NodeFactory<V> {
  @SuppressWarnings("rawtypes")
  private static final Node NULL_NODE = NullNode.instance();

  private static final Logger LOG = getLogger();

  private final Function<V, Bytes> valueSerializer;

  BinaryNodeFactory(final Function<V, Bytes> valueSerializer) {
    this.valueSerializer = valueSerializer;
  }

  @Override
  public Node<V> createExtension(final Bytes path, final Node<V> child) {
    return new ExtensionNode<>(path, child, this);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Node<V> createBranch(
      final byte leftIndex, final Node<V> left, final byte rightIndex, final Node<V> right) {
    assert (leftIndex <= BranchNode.RADIX);
    assert (rightIndex <= BranchNode.RADIX);
    assert (leftIndex != rightIndex);

    final ArrayList<Node<V>> children =
        new ArrayList<>(Collections.nCopies(2, (Node<V>) NULL_NODE));
    if (leftIndex == BranchNode.RADIX) {
      children.set(rightIndex, right);
      return createBranch(children, left.getValue());
    } else if (rightIndex == BranchNode.RADIX) {
      children.set(leftIndex, left);
      return createBranch(children, right.getValue());
    } else {
      children.set(leftIndex, left);
      children.set(rightIndex, right);
      return createBranch(children, Optional.empty());
    }
  }

  @Override
  public Node<V> createBranch(final ArrayList<Node<V>> children, final Optional<V> value) {
    return new BinaryBranchNode<>(children, value, this, valueSerializer);
  }

  @Override
  public Node<V> createLeaf(final Bytes path, final V value) {
    return new LeafNode<>(path, value, this, valueSerializer);
  }

  @Override
  public Node<V> createProofHash(final Bytes toHash) {
    return new MerkleProofHashNode<>(keccak256(toHash));
  }
}
